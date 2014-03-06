/**
 * 
 */
package com.brimud.filter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.BindingAnnotation;

/**
 * @author dan
 *
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandFilterChain {

}
