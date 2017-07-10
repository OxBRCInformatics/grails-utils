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

@SerializeMappings(nameMappings = ['fail:nest.pass', 'another:wibble'])
class SinglePushDownProcessTest {
    PushDown nest
    String wibble
    String colour
}

@SerializeMappings(nameMappings = ['fail:nest.pass', 'other_fail:other_nest.pass', 'another:wibble'])
class MultiplePushDownProcessTest {
    PushDown nest
    PushDown other_nest
    String wibble
}

@SerializeMappings(nameMappings = ['pass:passes'])
class SingleListProcessTest {
    List<String> passes
}

@SerializeMappings(nameMappings = ['pass:passes'])
class MapListProcessTest {
    List<SingleProcessTest> passes
}

@SerializeMappings(nameMappings = ['pass:passes', 'otherPass:otherPasses'])
class MultiMapListProcessTest {
    List<SingleProcessTest> passes
    List<SingleProcessTest> otherPasses
}

@SerializeMappings(nameMappings = ['pass:passes.pass'])
class PushDownListProcessTest {
    List<PushDown> passes
}


@SerializeMappings(nameMappings = ['fail:nest.pass', 'anotherFail:nest.otherPass'])
class MapPushDownProcessTest {
    MapPushDown nest
    String colour
}

class PushDown {
    String pass
}

class MapPushDown {
    String pass
    String otherPass
}