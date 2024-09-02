package com.emazon.stock.utils;

public class Constants {

    private Constants(){}

    public static final int MAX_CHARACTERS_NAME_CATEGORY = 50;
    public static final int MAX_CHARACTERS_DESCRIPTION_CATEGORY = 90;
    public static final int MIN_CHARACTERS = 1;
    public static final int MIN_CATEGORIES_BY_ARTICLE = 1;
    public static final int MAX_CATEGORIES_BY_ARTICLE = 3;
    public static final String ORDER_ASC = "ASC";
    public static final String DEFAULT_VALUE_SIZE_PAGE = "10";
    public static final String DEFAULT_VALUE_NUMBER_PAGE = "0";
    public static final int MIN_VALUES_PER_PAGE = 1;
    public static final int MIN_VALUE_PAGE = 0;
    public static final String REGEX_ORDER = "ASC|DESC";
    public static final int MAX_CHARACTERS_NAME_BRAND = 50;
    public static final int MAX_CHARACTERS_DESCRIPTION_BRAND = 120;


    //Exceptions messages
    public static final String EXCEPTION_CATEGORY_NAME_BLANK = "El nombre de la categoria no puede estar vacio.";
    public static final String EXCEPTION_CATEGORY_DESCRIPTION_BLANK = "La descripcion de la categoria no puede estar vacio.";
    public static final String EXCEPTION_CATEGORY_ALREADY_EXISTS = "La categoria con ese nombre ya existe.";
    public static final String EXCEPTION_CATEGORY_MAXIMUM_NUMBER_CHARACTERS_NAME = "El nombre de la categoria es muy largo, maximo 50 caracteres.";
    public static final String EXCEPTION_CATEGORY_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION = "La descripcion de la categoria es muy largo, maximo 90 caracteres.";
    public static final String EXCEPTION_CATEGORY_NO_DATA_FOUND  = "No se encontraron categorias registradas.";
    public static final String EXCEPTION_MIN_VALUES_PER_PAGE = "El minimo de resultados por pagina es de 1.";
    public static final String EXCEPTION_MIN_VALUE_PAGE = "El numero de pagina minimo es 0.";
    public static final String EXCEPTION_REGEX_ORDER = "No se encontró el orden solicitado.";
    public static final String EXCEPTION_BRAND_NAME_BLANK = "El nombre de la marca no puede estar vacio.";
    public static final String EXCEPTION_BRAND_DESCRIPTION_BLANK = "La descripcion de la marca no puede estar vacio.";
    public static final String EXCEPTION_BRAND_ALREADY_EXISTS = "La marca con ese nombre ya existe.";
    public static final String EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_NAME = "El nombre de la marca es muy largo, maximo 50 caracteres.";
    public static final String EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION = "La descripcion de la marca es muy largo, maximo 90 caracteres.";
    public static final String EXCEPTION_BRAND_NO_DATA_FOUND  = "No se encontraron marcas registradas.";
    public static final String EXCEPTION_ARTICLE_WITH_REPEATED_CATEGORIES = "El articulo no puede tener categorias repetidas.";
    public static final String EXCEPTION_ARTICLE_MINIMUM_CATEGORIES = "El minimo de categorias que se pueden asociar a un articulo es 1.";
    public static final String EXCEPTION_ARTICLE_EXCEEDS_CATEGORIES = "El maximo de categorias que se pueden asociar a un articulo es 3.";
    public static final String ARTICLE_NAME_MUST_MANDATORY = "El nombre del articulo es obligatorio.";
    public static final String ARTICLE_DESCRIPTION_MUST_MANDATORY = "La descripcion del articulo es obligatoria.";
    public static final String ARTICLE_QUANTITY_MUST_MANDATORY = "La cantidad del articulo es obligatoria.";
    public static final String ARTICLE_PRICE_MUST_MANDATORY = "El precio del articulo es obligatorio.";
    public static final String ARTICLE_BRAND_MUST_MANDATORY = "La marca del articulo es obligatoria.";
    public static final String EXCEPTION_ARTICLE_NO_DATA_FOUND = "No se encontraron articulos registrados.";

    public static final String ARTICLE_FIND_BY_NAME = "name";
    public static final String ARTICLE_FIND_BY_BRAND_NAME = "brand.name";
    public static final String REGEX_ORDERING_CRITERIA_ARTICLE = "brand|name";
}
