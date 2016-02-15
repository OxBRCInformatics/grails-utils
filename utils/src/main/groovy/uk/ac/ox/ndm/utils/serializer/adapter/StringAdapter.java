package uk.ac.ox.ndm.utils.serializer.adapter;

import com.google.common.base.Strings;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Fixes the empty string / null string issue. Ensures empty strings are rendered as null strings and therefore elements are not
 * rendered.
 */
public class StringAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String v) throws Exception {
        return v;
    }

    @Override
    public String marshal(String v) throws Exception {
        if (Strings.isNullOrEmpty(v)) {
            return null;
        }
        return v;
    }

}