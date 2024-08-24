package com.emazon.stock.utils;

public class Constants {

    private Constants(){}

    public static final int MAX_CHARACTERS_NAME_CATEGORY = 50;
    public static final int MAX_CHARACTERS_DESCRIPTION_CATEGORY = 90;
    public static final int MIN_CHARACTERS = 1;

    //Exceptions messages
    public static final String EXCEPTION_CATEGORY_NAME_BLANK = "El nombre de la categoria no puede estar vacio.";
    public static final String EXCEPTION_CATEGORY_DESCRIPTION_BLANK = "La descripcion de la categoria no puede estar vacio.";
    public static final String EXCEPTION_CATEGORY_ALREADY_EXISTS = "La categoria con ese nombre ya existe.";
    public static final String EXCEPTION_CATEGORY_MAXIMUM_NUMBER_CHARACTERS_NAME = "El nombre de la categoria es muy largo, maximo 50 caracteres.";
    public static final String EXCEPTION_CATEGORY_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION = "La descripcion de la categoria es muy largo, maximo 90 caracteres.";

}
