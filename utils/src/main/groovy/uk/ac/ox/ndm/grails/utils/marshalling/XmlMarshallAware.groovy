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
package uk.ac.ox.ndm.grails.utils.marshalling

import com.google.common.base.CaseFormat
import grails.converters.XML
import grails.converters.XML.Builder
import org.grails.web.converters.ConverterUtil
import org.grails.web.converters.configuration.ConverterConfiguration
import org.grails.web.converters.configuration.ConvertersConfigurationHolder
import uk.ac.ox.ndm.grails.utils.domain.DataType

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * @since 27/10/2015
 */
trait XmlMarshallAware {

    private XML xml
    private final ConverterConfiguration<XML> config = ConvertersConfigurationHolder.getConverterConfiguration(XML.class)

    String xmlElementName() {
        CaseFormat.UPPER_CAMEL.to CaseFormat.LOWER_CAMEL, getClass().simpleName
    }

    void marshallObject(XML xml) {
        this.xml = xml
        marshall()
    }

    abstract void marshall()

    void chars(String string) {
        xml.chars string
    }

    void child(XmlMarshallAware childObject) {
        child childObject.xmlElementName(), childObject
    }

    void child(String name, Object childObject) {
        xml.startNode name
        xml.convertAnother handleDouble(childObject)
        xml.end()
    }

    void child(String name, @DelegatesTo(Builder) Closure closure) {
        xml.startNode name
        xml.build(closure)
        xml.end()
    }

    void optionalChild(XmlMarshallAware childObject) {
        if (childObject) child childObject
    }

    void optionalChild(String name, Object childObject) {
        optionalChild name, childObject, childObject != null
    }

    void optionalChild(String name, Object childObject, def exists) {
        if (exists) child name, childObject
    }

    void optionalChild(String name, @DelegatesTo(Builder) Closure closure) {
        xml.startNode name
        xml.build(closure)
        xml.end()
    }

    void childList(String childName, List list) {
        list?.each {child childName, it}
    }

    void childList(List list) {
        list?.each {child it}
    }

    void childCollection(String childName, Collection collection) {
        if (collection) {
            collection[0].hasProperty('id') ?
            childList(childName, collection?.sort {it?.id}) :
            childList(childName, collection?.sort())
        }
    }

    void childCollection(Collection collection) {
        if (collection) {
            collection[0].hasProperty('id') ?
            childList(collection?.sort {it?.id}) :
            childList(collection?.sort())
        }
    }

    void choice(Map<String, Object> optionObjects) {
        def childObject = optionObjects.find {k, v -> v}
        child childObject.key, childObject.value
    }

    void choiceChild(String childName, Map<String, Object> optionObjects) {
        xml.startNode childName
        choice(optionObjects)
        xml.end()
    }

    void optionalChoice(Map<String, Object> optionObjects) {
        def childObject = optionObjects.find {k, v -> v}
        if (childObject) child childObject.key, childObject.value
    }

    void optionalChoiceChild(String childName, Map<String, Object> optionObjects) {
        if (optionObjects.any {k, v -> v}) choiceChild childName, optionObjects
    }

    @Deprecated
    void childAttribute(String name, String attributeName, Object childObject) {
        if (childObject) {
            xml.startNode name
            attribute attributeName, childObject
            xml.end()
        }
    }

    void childAttribute(String nodeName, Map attributes, Object childObject) {
        if (attributes) {
            xml.startNode nodeName
            attributes.each {k, v ->
                attribute convertToString(k), v
            }
            xml.convertAnother handleDouble(childObject)
            xml.end()
        }
    }

    void optionalChildAttribute(String name, Map attributes, Object childObject) {
        if (childObject)
            childAttribute(name, attributes, childObject)
    }


    void attribute(String name, Object childObject) {
        xml.attribute name, convertToString(childObject)
    }

    void node(String tagName, @DelegatesTo(XmlMarshallAware) Closure closure) {
        startNode tagName
        closure.delegate = this
        closure.run()
        endNode()
    }

    void startNode(String tagName) {
        xml.startNode tagName
    }

    void end() {
        xml.end()
    }

    void endNode() {
        xml.end()
    }

    void build(@DelegatesTo(Builder) Closure closure) {
        xml.build closure
    }

    void expandChild(String name, XmlMarshallAware... childObject) {
        xml.startNode name
        childObject*.marshallObject(xml)
        xml.end()
    }

    void expandChild(XmlMarshallAware childObject) {
        childObject?.marshallObject(xml)
    }

    String convertToString(Object childObject) {
        childObject = config.getProxyHandler().unwrapIfProxy(childObject);

        try {
            if (childObject == null) return null
            if (childObject instanceof CharSequence) return childObject.toString()
            if (childObject instanceof Class<?>) return ((Class<?>) childObject).getName()
            if ((childObject.getClass().isPrimitive() && childObject.getClass() != byte[].class) ||
                childObject instanceof Number || childObject instanceof Boolean) return String.valueOf(handleDouble(childObject))
            if (childObject instanceof DataType) return childObject.id
            return childObject.toString()
        }
        catch (Throwable t) {
            throw ConverterUtil.resolveConverterException(t);
        }
    }

    Object handleDouble(Object value) {
        if (value instanceof Double || value.getClass() == double.class) {

            DecimalFormat decimalFormat = new DecimalFormat('0.00', DecimalFormatSymbols.getInstance(Locale.default))
            decimalFormat.setMaximumFractionDigits(340)
            return decimalFormat.format(value)
        }
        value
    }
}
