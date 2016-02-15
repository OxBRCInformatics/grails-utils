# Grails Utils

## Usage

This is designed to be used inside a Grails application/plugin adding extra support for redefining different aspects of default
 Grails systems.

 Validators have been added which can be used to augment domain constraint validation, for them to work the following will
 needed to be added to the `messages.properties` file:

 ```
validation.choice.onlyone=Property {0} with value {2} is invalid, only one {3} may be set
validation.choice.atleastone=Property {0} with value {2} is invalid, at least one {3} must be set
validation.nhsnumber.wronglength=Property {0} with value {2} is invalid, it must be at least 10 digits for a valid NHS number
validation.empty=Property {0} is invalid, it cannot be empty
validation.hasmany.size.atleast=Set must have at least {3} element(s)
validation.hasmany.size.atmost=Set cannot have more than {3} element(s)
validation.schema.version=Schema {3} version with value {2} must be one of {4}
validation.nhsnumber.doesnotmatch=NHS number with value [{2}] for participant ID [{3}] does not equal existing NHS number [{4}]
validation.list.pattern.invalid=String {0} in list {1} does not match pattern [{3}]
validation.message.schema.name=Message of type {2} does not match expected schema name {3}
 ```

More info to follow...