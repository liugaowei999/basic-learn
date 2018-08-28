package com.cttic.liugw.ordinary.IOC;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.beans.factory.annotation.Qualifier;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface MusicGenre {
    public enum GENRE {
        CLASSICAL, METAL, ROCK, TRANCE
    }

    //    Genre genre() default Genre.TRANCE;
}

class Genre {
    public enum GENRE {
        CLASSICAL, METAL, ROCK, TRANCE
    }

    public static GENRE TRANCE;
}