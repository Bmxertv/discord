package de.bmxertv.discord.api.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Die Annotation um ein Command zu kennzeichnen.
 *
 * @version 1.0
 * @author Christopher
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * Name und Call des Commands.
     * @return den Command Call
     */
    String value();

    /**
     * Sub Commands die angezeigt werden sollen.
     * @return Liste von Strings
     */
    String[] subCommands() default {};

    /**
     * Die Beschreibung des Commandes.
     * @return die Beschreibung
     */
    String description() default "";

    /**
     * Weiter Calls die genutzt werden können.
     * @return Liste von Strings
     */
    String[] aliases() default {};

}
