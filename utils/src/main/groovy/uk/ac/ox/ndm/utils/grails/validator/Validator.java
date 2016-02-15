package uk.ac.ox.ndm.utils.grails.validator;

/**
 * @since 14/08/2015
 */
public interface Validator<T> {

    Object isValid(T value);
}
