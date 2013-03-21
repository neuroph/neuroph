package org.neuroph.netbeans.main.easyneurons.file;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import javax.swing.JComponent;

public class SwingXmlConverter implements Converter {

	@Override
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class arg0) {
		return JComponent.class.isAssignableFrom(arg0);
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0, UnmarshallingContext arg1) {
		return null;
	}
}
