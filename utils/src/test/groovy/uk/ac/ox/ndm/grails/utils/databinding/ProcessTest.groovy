package uk.ac.ox.ndm.grails.utils.databinding

import uk.ac.ox.ndm.grails.utils.serializer.SerializeMappings

/**
 * @since 07/04/2016
 */
class NoProcessTest {
    String pass
}

@SerializeMappings(nameMappings = ['fail:pass'])
class SingleProcessTest {
    String pass
}

@SerializeMappings(nameMappings = ['fail:pass', 'another:wibble'])
class TwoProcessTest {
    String pass
    String wibble
}

@SerializeMappings(nameMappings = ['fail:pass', 'another:wibble'])
class MixedSimpleProcessTest {
    String pass
    String wibble
    String colour
}

@SerializeMappings(nameMappings = ['nest.fail:pass'])
class SingleNestedProcessTest {
    String pass
}

@SerializeMappings(nameMappings = ['nest.fail:pass', 'nest.another:wibble'])
class TwoMultipleSameProcessTest {
    String pass
    String wibble
}

@SerializeMappings(nameMappings = ['nest.fail:pass', 'nest.another:wibble'])
class TwoMultipleSameMixedProcessTest {
    String pass
    String wibble
    String colour
}

@SerializeMappings(nameMappings = ['nest.fail:pass', 'diff.another:wibble'])
class TwoMultipleDifferentProcessTest {
    String pass
    String wibble
}