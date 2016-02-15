package uk.ac.ox.ndm.grails.utils.validator

/**
 * @since 10/11/2015
 */
class StringListPatternValidator implements Validator<Collection<String>> {

    String name
    String pattern

    StringListPatternValidator(String listName, String pattern) {
        this.pattern = pattern
        this.name = listName
    }

    @Override
    Object isValid(Collection<String> value) {
        String val = value.find {!(it ==~ pattern)}
        val ? ["validation.list.pattern.invalid", val, name, pattern] : true
    }
}
