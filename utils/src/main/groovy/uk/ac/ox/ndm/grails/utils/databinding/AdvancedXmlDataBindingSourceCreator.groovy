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

import grails.databinding.CollectionDataBindingSource
import grails.databinding.DataBindingSource
import grails.web.mime.MimeType
import grails.web.servlet.mvc.GrailsParameterMap
import groovy.transform.CompileStatic
import groovy.util.slurpersupport.GPathResult
import org.grails.databinding.bindingsource.DataBindingSourceCreationException
import org.grails.databinding.xml.GPathResultCollectionDataBindingSource
import org.grails.databinding.xml.GPathResultMap
import org.grails.io.support.SpringIOUtils
import org.grails.web.databinding.bindingsource.InvalidRequestBodyException
import org.xml.sax.SAXParseException

import javax.servlet.http.HttpServletRequest

/**
 * @since 02/09/2015
 */
@CompileStatic
class AdvancedXmlDataBindingSourceCreator extends AdvancedDataBindingSourceCreator {

    @Override
    MimeType[] getMimeTypes() {
        [MimeType.XML, MimeType.TEXT_XML] as MimeType[]
    }

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

    DataBindingSource createDataBindingSource(Reader reader, Class bindingTargetType) {
        createDataBindingSource(SpringIOUtils.createXmlSlurper().parse(reader), bindingTargetType)
    }

    DataBindingSource createDataBindingSource(GPathResult gPathResult, Class bindingTargetType) {
        createDataBindingSource(new GPathResultMap(gPathResult), bindingTargetType)
    }

    DataBindingSourceCreationException createBindingSourceCreationException(Exception e) {
        if (e instanceof SAXParseException) {
            logger.error "Exception while creating databinding source: {}", e.message
            return new InvalidRequestBodyException(e)
        }
        return super.createBindingSourceCreationException(e)
    }
}
