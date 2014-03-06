package com.brimud.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.brimud.command.Direction;
import com.brimud.db.RoomDao;
import com.brimud.db.Transacted;
import com.brimud.db.ZoneDao;
import com.brimud.zone.DirectionType;
import com.brimud.zone.ExtraType;
import com.brimud.zone.ExtraType.Extra;
import com.brimud.zone.RoomType;
import com.brimud.world.WorldType;
import com.brimud.world.ZoneLinksType;
import com.brimud.world.ZoneLinksType.Link;
import com.brimud.zone.ZoneType;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.model.Zone;
import com.brimud.service.BuilderException;
import com.brimud.service.BuilderService;
import com.google.inject.Inject;

public class Importer {

  private static final Logger logger = LoggerFactory.getLogger(Importer.class);

  private final ZoneDao zoneDao;
  
  private final RoomDao roomDao;
  
  private final BuilderService builderService;

  @Inject
  public Importer(ZoneDao zoneDao, RoomDao roomDao, BuilderService builderService) {
    this.zoneDao = zoneDao;
    this.roomDao = roomDao;
    this.builderService = builderService;
  }

  // TODO separate Importer/Exporter functionality into separate database/xml
  // components
  @Transacted
  public void importZone(String fileName) {
    useInputSource(fileName, new InputSourceVisitor() {
      @Override
      public void visit(InputSource inputSource) {
        ZoneType zoneType = parseZone(inputSource);
        Zone zone = mapToXml(zoneType);
        
        // delete all the rooms before adding them back in
        for (Room room : zone.getRooms()) {
        	try {
        		builderService.deleteRoom(room);
        	} catch (BuilderException e) {
        		e.printStackTrace();
        	}
        }
        zoneDao.saveOrUpdate(zone);
      }
    });
  }
  @Transacted
  public void importWorld(String fileName) {

    useInputSource(fileName, new InputSourceVisitor() {
      @Override
      public void visit(InputSource inputSource) {

        WorldType worldType = parseWorld(inputSource);

        List<ZoneType> zoneTypes = worldType.getZone();

        for (ZoneType zoneType : zoneTypes) {
          Zone zone = mapToXml(zoneType);

          // delete all the rooms before adding them back in
          for (Room room : zone.getRooms()) {
          	roomDao.delete(room);
          }
          zoneDao.saveOrUpdate(zone);
        }
        
        // update the links between zones
        ZoneLinksType zoneLinks = worldType.getZonelinks();
        if (zoneLinks != null) {
        	for (Link link : zoneLinks.getLink()) {
        		Room fromRoom = roomDao.getById(new RoomId(link.getFromZone(), link.getFromRoom()));
        		Room toRoom = roomDao.getById(new RoomId(link.getToZone(), link.getToRoom()));
        		String stringDirection = link.getDirection();
        		Direction direction = Direction.fromName(stringDirection);
        		fromRoom.addExit(direction, toRoom);
        	}
        }
      }
    });
  }

  private static interface InputSourceVisitor {
    void visit(InputSource inputSource);
  }

  private void useInputSource(String fileName, InputSourceVisitor visitor) {
    FileInputStream fileStream = null;
    try {
      fileStream = new FileInputStream(fileName);
      InputSource source = new InputSource(fileStream);
      visitor.visit(source);
    } catch (FileNotFoundException e) {
      logger.error("Couldn't read file: " + fileName, e);
    } finally {
      try {
        if (fileStream != null) {
          fileStream.close();
        }
      } catch (IOException e) {
        logger.warn("Couldn't close file stream on file: " + fileName, e);
      }
    }
  }

  ZoneType parseZone(InputSource inputSource) {
    return parse(inputSource, ZoneType.class, "zone.xsd");
  }

  WorldType parseWorld(InputSource inputSource) {
    return parse(inputSource, WorldType.class, "world.xsd");
  }

  private <T> T parse(InputSource inputSource, Class<T> jaxbClass,
      String schemaName) {
    try {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);

      // validates using the schema
      spf.setSchema(loadSchema(schemaName));

      // The SAX parser is used to process includes
      spf.setXIncludeAware(true);
      SAXParser saxParser = spf.newSAXParser();

      XMLReader xmlReader = saxParser.getXMLReader();
      SAXSource source = new SAXSource(xmlReader, inputSource);

      JAXBContext jc = JAXBContext
          .newInstance(jaxbClass.getPackage().getName());
      Unmarshaller u = jc.createUnmarshaller();
      return u.unmarshal(source, jaxbClass).getValue();
    } catch (Exception e) {
      throw new RuntimeException("Error parsing file", e);
    }
  }

  Zone mapToXml(ZoneType zoneType) {
    Zone zone = new Zone(zoneType.getId());
    zone.setName(zoneType.getName());
    zone.setDescription(zoneType.getDescription());
    zone.setShortDescription(zoneType.getShort());
    zone.setStartingZone(zoneType.isStartingZone());

    // load all the rooms first
    for (RoomType roomType : zoneType.getRoom()) {
      Room room = new Room();
      room.setId(new RoomId(zone, roomType.getId()));
      room.setName(roomType.getName());
      room.setDescription(roomType.getDescription());
      room.setShortDescription(roomType.getShort());
      zone.addRoom(room);

      // and all the extras for the room
      ExtraType extraTypes = roomType.getExtras();
      if (extraTypes != null) {
        List<Extra> extras = extraTypes.getExtra();
        for (Extra extra : extras) {
          room.addExtraDescription(extra.getKeyword(), extra.getValue());
        }
      }
    }

    // then set the starting room
    zone.setStartingRoom(zoneType.getStartingRoom());

    // then load all the exits
    for (RoomType roomType : zoneType.getRoom()) {
      Room room = zone.getRoom(roomType.getId());

      loadExit(zone, room, roomType.getNorth(), Direction.NORTH);
      loadExit(zone, room, roomType.getSouth(), Direction.SOUTH);
      loadExit(zone, room, roomType.getEast(), Direction.EAST);
      loadExit(zone, room, roomType.getWest(), Direction.WEST);
      loadExit(zone, room, roomType.getUp(), Direction.UP);
      loadExit(zone, room, roomType.getDown(), Direction.DOWN);
    }

    return zone;
  }

  private void loadExit(Zone zone, Room room, DirectionType dirType,
      Direction dir) {
    if (dirType != null) {
      Room exit = zone.getRoom(dirType.getExitToRoom());
      room.addExit(dir, exit);
      room.addScanDescription(dir, dirType.getValue());
    }
  }

  private Schema loadSchema(final String schemaName) {
    SchemaFactory schemaFactory = SchemaFactory
        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    schemaFactory.setResourceResolver(new LSResourceResolver() {
      @Override
      public LSInput resolveResource(String type, String namespaceURI,
          String publicId, String systemId, String baseURI) {
        if ("zone.xsd".equals(systemId))
          return new LSInputAdapter() {
            @Override
            public InputStream getByteStream() {
              return getClass().getResourceAsStream("/zone.xsd");
            }
          };
        return null;
      }
    });
    StreamSource xsdSource = new StreamSource(getClass().getResourceAsStream(
        "/" + schemaName), schemaName);
    try {
      return schemaFactory.newSchema(xsdSource);
    } catch (SAXException e) {
      throw new RuntimeException("Error loading schema", e);
    }
  }
}
