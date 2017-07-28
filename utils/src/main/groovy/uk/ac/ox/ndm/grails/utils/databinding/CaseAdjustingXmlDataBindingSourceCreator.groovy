/*
 * Academic Use Licence
 *
 * These licence terms apply to all licences granted by
 * OXFORD UNIVERSITY INNOVATION LIMITED whose administrative offices are at
 * University Offices, Wellington Square, Oxford OX1 2JD, United Kingdom ("OUI")
 * for use of Grails Utils, a generic set of libraries used by MeRCURY and BuRST
 * for data manipulation and validation, message passing, and Grails configuration
 * ("the Software") through this website
 * https://github.com/OxBRCInformatics/grails-utils (the "Website").
 *
 * PLEASE READ THESE LICENCE TERMS CAREFULLY BEFORE DOWNLOADING THE SOFTWARE
 * THROUGH THIS WEBSITE. IF YOU DO NOT AGREE TO THESE LICENCE TERMS YOU SHOULD NOT
 * [REQUEST A USER NAME AND PASSWORD OR] DOWNLOAD THE SOFTWARE.
 *
 * THE SOFTWARE IS INTENDED FOR USE BY ACADEMICS CARRYING OUT RESEARCH AND NOT FOR
 * USE BY CONSUMERS OR COMMERCIAL BUSINESSES.
 *
 * 1. Academic Use Licence
 *
 *   1.1 The Licensee is granted a limited non-exclusive and non-transferable
 *       royalty free licence to download and use the Software provided that the
 *       Licensee will:
 *
 *       (a) limit their use of the Software to their own internal academic
 *           non-commercial research which is undertaken for the purposes of
 *           education or other scholarly use;
 *
 *       (b) not use the Software for or on behalf of any third party or to
 *           provide a service or integrate all or part of the Software into a
 *           product for sale or license to third parties;
 *
 *       (c) use the Software in accordance with the prevailing instructions and
 *           guidance for use given on the Website and comply with procedures on
 *           the Website for user identification, authentication and access;
 *
 *       (d) comply with all applicable laws and regulations with respect to their
 *           use of the Software; and
 *
 *       (e) ensure that the Copyright Notice (c) 2016, Oxford University
 *           Innovation Ltd." appears prominently wherever the Software is
 *           reproduced and is referenced or cited with the Copyright Notice when
 *           the Software is described in any research publication or on any
 *           documents or other material created using the Software.
 *
 *   1.2 The Licensee may only reproduce, modify, transmit or transfer the
 *       Software where:
 *
 *       (a) such reproduction, modification, transmission or transfer is for
 *           academic, research or other scholarly use;
 *
 *       (b) the conditions of this Licence are imposed upon the receiver of the
 *           Software or any modified Software;
 *
 *       (c) all original and modified Source Code is included in any transmitted
 *           software program; and
 *
 *       (d) the Licensee grants OUI an irrevocable, indefinite, royalty free,
 *           non-exclusive unlimited licence to use and sub-licence any modified
 *           Source Code as part of the Software.
 *
 *     1.3 OUI reserves the right at any time and without liability or prior
 *         notice to the Licensee to revise, modify and replace the functionality
 *         and performance of the access to and operation of the Software.
 *
 *     1.4 The Licensee acknowledges and agrees that OUI owns all intellectual
 *         property rights in the Software. The Licensee shall not have any right,
 *         title or interest in the Software.
 *
 *     1.5 This Licence will terminate immediately and the Licensee will no longer
 *         have any right to use the Software or exercise any of the rights
 *         granted to the Licensee upon any breach of the conditions in Section 1
 *         of this Licence.
 *
 * 2. Indemnity and Liability
 *
 *   2.1 The Licensee shall defend, indemnify and hold harmless OUI against any
 *       claims, actions, proceedings, losses, damages, expenses and costs
 *       (including without limitation court costs and reasonable legal fees)
 *       arising out of or in connection with the Licensee's possession or use of
 *       the Software, or any breach of these terms by the Licensee.
 *
 *   2.2 The Software is provided on an "as is" basis and the Licensee uses the
 *       Software at their own risk. No representations, conditions, warranties or
 *       other terms of any kind are given in respect of the the Software and all
 *       statutory warranties and conditions are excluded to the fullest extent
 *       permitted by law. Without affecting the generality of the previous
 *       sentences, OUI gives no implied or express warranty and makes no
 *       representation that the Software or any part of the Software:
 *
 *       (a) will enable specific results to be obtained; or
 *
 *       (b) meets a particular specification or is comprehensive within its field
 *           or that it is error free or will operate without interruption; or
 *
 *       (c) is suitable for any particular, or the Licensee's specific purposes.
 *
 *   2.3 Except in relation to fraud, death or personal injury, OUI"s liability to
 *       the Licensee for any use of the Software, in negligence or arising in any
 *       other way out of the subject matter of these licence terms, will not
 *       extend to any incidental or consequential damages or losses, or any loss
 *       of profits, loss of revenue, loss of data, loss of contracts or
 *       opportunity, whether direct or indirect.
 *
 *   2.4 The Licensee hereby irrevocably undertakes to OUI not to make any claim
 *       against any employee, student, researcher or other individual engaged by
 *       OUI, being a claim which seeks to enforce against any of them any
 *       liability whatsoever in connection with these licence terms or their
 *       subject-matter.
 *
 * 3. General
 *
 *   3.1 Severability - If any provision (or part of a provision) of these licence
 *       terms is found by any court or administrative body of competent
 *       jurisdiction to be invalid, unenforceable or illegal, the other
 *       provisions shall remain in force.
 *
 *   3.2 Entire Agreement - These licence terms constitute the whole agreement
 *       between the parties and supersede any previous arrangement, understanding
 *       or agreement between them relating to the Software.
 *
 *   3.3 Law and Jurisdiction - These licence terms and any disputes or claims
 *       arising out of or in connection with them shall be governed by, and
 *       construed in accordance with, the law of England. The Licensee
 *       irrevocably submits to the exclusive jurisdiction of the English courts
 *       for any dispute or claim that arises out of or in connection with these
 *       licence terms.
 *
 * If you are interested in using the Software commercially, please contact
 * Oxford University Innovation Limited to negotiate a licence.
 * Contact details are enquiries@innovation.ox.ac.uk quoting reference 14422.
 */
package uk.ac.ox.ndm.grails.utils.databinding

import com.google.common.base.CaseFormat
import grails.databinding.CollectionDataBindingSource
import grails.databinding.DataBindingSource
import grails.databinding.SimpleMapDataBindingSource
import grails.util.Holders
import grails.validation.ValidationException
import grails.web.mime.MimeType
import grails.web.servlet.mvc.GrailsParameterMap
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import groovy.util.slurpersupport.GPathResult
import org.grails.databinding.bindingsource.DataBindingSourceCreationException
import org.grails.databinding.xml.GPathResultCollectionDataBindingSource
import org.grails.databinding.xml.GPathResultMap
import org.grails.io.support.SpringIOUtils
import org.grails.web.databinding.bindingsource.DefaultDataBindingSourceCreator
import org.grails.web.databinding.bindingsource.InvalidRequestBodyException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.xml.sax.SAXParseException
import uk.ac.ox.ndm.grails.utils.Utils
import uk.ac.ox.ndm.grails.utils.databinding.bindingsource.AbstractDomainDataBindingSource
import uk.ac.ox.ndm.grails.utils.databinding.bindingsource.DataTypeDataBindingSource
import uk.ac.ox.ndm.grails.utils.domain.DataType
import uk.ac.ox.ndm.grails.utils.serializer.SerializeMappings

import javax.servlet.http.HttpServletRequest
import java.lang.annotation.Annotation
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * @since 02/09/2015
 * @deprecated use {@link uk.ac.ox.ndm.grails.utils.databinding.AdvancedXmlDataBindingSourceCreator} instead
 */
@CompileStatic
@Deprecated
class CaseAdjustingXmlDataBindingSourceCreator extends DefaultDataBindingSourceCreator implements Utils {

    private static final Logger logger = LoggerFactory.getLogger(CaseAdjustingXmlDataBindingSourceCreator)

    @Autowired
    MessageSource messageSource

    @Autowired
    ApplicationContext applicationContext

    private Collection<XmlDataBindingSourceCreatorHelper> postProcessors

    @Override
    MimeType[] getMimeTypes() {
        [MimeType.XML, MimeType.TEXT_XML] as MimeType[]
    }

    protected boolean initialised

    CaseAdjustingXmlDataBindingSourceCreator() {
        initialised = false
        postProcessors = []
    }

    CaseAdjustingXmlDataBindingSourceCreator(ApplicationContext applicationContext) {
        this()
        this.applicationContext = applicationContext
        initialise()
    }

    CaseAdjustingXmlDataBindingSourceCreator(Collection<XmlDataBindingSourceCreatorHelper> postProcessors) {
        this()
        initialise postProcessors
    }

    void initialise() {
        if (!applicationContext) applicationContext = Holders.getApplicationContext()
        initialise applicationContext.getBeansOfType(XmlDataBindingSourceCreatorHelper).values()
    }

    void initialise(XmlDataBindingSourceCreatorHelper postProcessor) {
        initialise([postProcessor])
    }

    void initialise(Collection<XmlDataBindingSourceCreatorHelper> postProcessors) {
        this.postProcessors = postProcessors
        initialised = true
    }


    // Collection value data binding processing

    @Override
    CollectionDataBindingSource createCollectionDataBindingSource(MimeType mimeType, Class bindingTargetType,
                                                                  Object bindingSource) {
        if (bindingSource instanceof GPathResult) {
            new GPathResultCollectionDataBindingSource(bindingSource)
        }
        else {
            try {
                if (bindingSource instanceof GrailsParameterMap) {
                    def req = bindingSource.getRequest()
                    def is = req.getInputStream()
                    return createCollectionBindingSource(is, req.getCharacterEncoding())
                }
                if (bindingSource instanceof HttpServletRequest) {
                    def req = bindingSource as HttpServletRequest
                    def is = req.getInputStream()
                    return createCollectionBindingSource(is, req.getCharacterEncoding())
                }
                if (bindingSource instanceof InputStream) {
                    def is = bindingSource as InputStream
                    return createCollectionBindingSource(is, "UTF-8")
                }
                if (bindingSource instanceof Reader) {
                    def is = bindingSource as Reader
                    return createCollectionBindingSource(is)
                }

                return super.createCollectionDataBindingSource(mimeType, bindingTargetType, bindingSource)
            } catch (Exception e) {
                throw new DataBindingSourceCreationException(e)
            }
        }
    }

    CollectionDataBindingSource createCollectionBindingSource(InputStream inputStream, String charsetName) {
        createCollectionBindingSource(new InputStreamReader(inputStream, charsetName ?: 'UTF-8'))
    }

    CollectionDataBindingSource createCollectionBindingSource(Reader reader) {
        new GPathResultCollectionDataBindingSource(SpringIOUtils.createXmlSlurper().parse(reader))
    }

    // Single value data binding processing

    @Override
    DataBindingSource createDataBindingSource(MimeType mimeType, Class bindingTargetType, Object bindingSource) {
        try {
            if (bindingSource instanceof String) {
                GPathResult result = new XmlSlurper().parseText(new String(bindingSource))
                return createDataBindingSource(result, bindingTargetType)
            }
            if (bindingSource instanceof GPathResult) {
                return createDataBindingSource(bindingSource, bindingTargetType)
            }
            if (bindingSource instanceof HttpServletRequest) {
                def req = bindingSource as HttpServletRequest
                def is = req.getInputStream()
                return createDataBindingSource(is, req.getCharacterEncoding(), bindingTargetType)
            }
            if (bindingSource instanceof InputStream) {
                def is = bindingSource as InputStream
                return createDataBindingSource(is, "UTF-8", bindingTargetType)
            }
            if (bindingSource instanceof Reader) {
                def is = bindingSource as Reader
                return createDataBindingSource(is, bindingTargetType)
            }
            return super.createDataBindingSource(mimeType, bindingTargetType, bindingSource)
        } catch (Exception e) {
            throw createBindingSourceCreationException(e)
        }
    }

    DataBindingSource createDataBindingSource(InputStream inputStream, String charsetName, Class bindingTargetType) {
        createDataBindingSource(new InputStreamReader(inputStream, charsetName ?: 'UTF-8'), bindingTargetType)
    }

    DataBindingSource createDataBindingSource(Reader reader, Class bindingTargetType) {
        createDataBindingSource(SpringIOUtils.createXmlSlurper().parse(reader), bindingTargetType)
    }

    DataBindingSource createDataBindingSource(GPathResult gPathResult, Class bindingTargetType) {
        createDataBindingSource(new GPathResultMap(gPathResult), bindingTargetType)
    }

    DataBindingSource createDataBindingSource(Map<String, ?> input, Class bindingTargetType) {
        Map<String, ?> preprocessed = preProcessDataBindingMap(input)
        Object processed = processDataBindingMap(preprocessed, bindingTargetType)
        if (processed instanceof Map) return new SimpleMapDataBindingSource(processed)
        if (processed instanceof DataBindingSource) return processed as DataBindingSource

        throw new InvalidClassException('Processed value class ' + processed.class.canonicalName +
                                        ' is not Map or DataBindingSource')
    }

    Object processDataBindingMap(Map<String, ?> input, Class bindingTargetType) {

        Annotation mappings = bindingTargetType.getAnnotation(SerializeMappings) ?:
                              bindingTargetType.superclass?.getAnnotation(SerializeMappings)

        Map<String, Object> keyMappings = extractNameMappings(mappings?.nameMappings())

        Map<String, Object> processedDataBindingMap = [:]

        // Convert the input map into a valid databinding map
        input.each {k, v ->
            Object validKey = createValidKey(k, keyMappings)
            if (validKey instanceof Map) {
                Map<String, ?> keyValue = extractKeyValuePairFromKeyMapping(validKey as Map<String, String>, v)

                keyValue.each {mappedKey, mappedValue ->
                    Object value = convertValue(mappedValue, mappedKey, bindingTargetType, processedDataBindingMap)
                    processedDataBindingMap.put(mappedKey, value)
                }
            }
            else {
                Object value = convertValue(v, validKey as String, bindingTargetType, processedDataBindingMap)
                processedDataBindingMap.put(validKey as String, value)
            }

        }

        checkProcessDataBindingMap(processedDataBindingMap, bindingTargetType)

    }


    Object convertValue(Object v, String key, Class bindingTargetType, Map<String, ?> converted) {
        def value = v
        if (v instanceof List) {
            value = converted.getOrDefault(key, [])
            v.each {
                value = convertAndAddToList(key, it, bindingTargetType, value as List)
            }
        }
        else {
            Class bindingType = determineBindingType(bindingTargetType, key)
            if (v instanceof Map) {
                if (!bindingType) {
                    throw new IllegalStateException(
                            "There must be a property in " + bindingTargetType.canonicalName + " for key " + key)
                }

                if (Collection.isAssignableFrom(bindingType)) {
                    return convertAndAddToList(key, value, bindingTargetType, converted.getOrDefault(key, []) as List)
                }
                value = processDataBindingMap(value as Map<String, ?>, bindingType)
            }
            else if (bindingType && DataType.isAssignableFrom(bindingType)) {
                value = new DataTypeDataBindingSource([key: value], bindingType)
            }
        }
        value
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    Class determineBindingType(Class bindingTargetType, String key) {
        MetaProperty metaProperty = bindingTargetType.metaClass.getMetaProperty(key)
        if (!metaProperty && bindingTargetType.superclass != Object) {
            return determineBindingType(bindingTargetType.superclass, key)
        }

        def bindingType = metaProperty?.field?.type ?: metaProperty?.type
        if (bindingType == Object && bindingTargetType.superclass != Object) {
            return determineBindingType(bindingTargetType.superclass, key)
        }
        if (bindingType == Object) {
            String methodSetterName = "set${CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, key)}".toString()
            Method setter = bindingTargetType.methods.find {it.name == methodSetterName}
            if (setter) return setter.parameterTypes[0]
        }
        bindingType
    }

    Object convertAndAddToList(String key, Object value, Class bindingTargetType, List listValue) {

        if (value instanceof Map) {
            def bindingType = getReferencedTypeForCollectionInClass(key, bindingTargetType)
            if (!bindingType) {
                throw new IllegalStateException("There must be a binding type in " + bindingTargetType.canonicalName + " for key "
                                                        + key)
            }
            def output = processDataBindingMap(value, bindingType)
            listValue += output instanceof AbstractDomainDataBindingSource ? output.getDomain() : output

        }
        else listValue += value

        listValue
    }

    static Map<String, ?> extractKeyValuePairFromKeyMapping(Map<String, String> keys, Object value) {

        if (!value) return [:]
        Map<String, ?> result = [:]

        keys.each {key, mapping ->

            if (value[key]) {
                if (mapping instanceof Map) {
                    result.putAll(extractKeyValuePairFromKeyMapping(mapping as Map<String, String>, value[key]))

                }
                else result[mapping] = value[key]
            }

        }

        result
    }

    static Map<String, ?> preProcessDataBindingMap(Map<String, ?> map) {
        if (map == null) throw new IllegalArgumentException('null map is not permitted for pre processing')
        map.collectEntries {k, v ->
            [(createValidKey(k, [:])): (preProcessDataBindingValue(v))]
        } as Map<String, ?>
    }

    Object checkProcessDataBindingMap(Map<String, ?> processDataBindingMap, Class bindingTargetType) {

        if (!initialised) initialise()

        for (XmlDataBindingSourceCreatorHelper postProcessor : postProcessors) {
            def obj = postProcessor.checkDataBindingSourceMap(processDataBindingMap, bindingTargetType)
            if (obj) return obj
        }
        processDataBindingMap
    }

    static Object preProcessDataBindingValue(Object obj) {
        obj instanceof Map ?
        preProcessDataBindingMap(obj) :
        obj instanceof Collection ?
        obj.collect {preProcessDataBindingValue(it)} :
        obj
    }

    static Object createValidKey(String k, Map keyMappings) {
        String key = (k.contains('-') ? CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, k) :
                      CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, k)).replaceAll(/\./, '')
        keyMappings ? (key in keyMappings.keySet() ? keyMappings[key] : key) : key
    }

    static Class<?> getReferencedTypeForCollectionInClass(String propertyName, Class clazz) {
        def field = getField(clazz, propertyName)
        if (field) {
            def genericType = field.genericType
            if (genericType instanceof ParameterizedType) {
                return Map.isAssignableFrom(genericType.getRawType() as Class<?>) ?
                       genericType.getActualTypeArguments()[1] : genericType.getActualTypeArguments()[0]
            }
        }
        null
    }

    static Field getField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName)
        } catch (NoSuchFieldException ignored) {
            return clazz.getSuperclass() != Object ? getField(clazz.getSuperclass(), fieldName) : null
        }
    }

    DataBindingSourceCreationException createBindingSourceCreationException(Exception e) {

        if (e instanceof SAXParseException) {
            logger.error "Exception while creating databinding source: {}", e.message
            return new InvalidRequestBodyException(e)
        }
        if (e instanceof ValidationException) {
            StringBuilder sb = new StringBuilder()
            e.errors.allErrors.each {error ->
                sb.append(messageSource.getMessage(error, Locale.default)).append('\n')
            }
            logger.error "Exception while creating databinding source: {}", sb.toString()
        }
        else logger.error "Exception while creating databinding source: " + e.message, e
        return new DataBindingSourceCreationException(e)
    }
}
