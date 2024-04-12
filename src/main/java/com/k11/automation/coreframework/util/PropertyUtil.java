package com.k11.automation.coreframework.util;

import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import org.apache.commons.configuration.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PropertyUtil extends XMLConfiguration {

	private static final long serialVersionUID = -8633909707831110230L;
	Log logger = LogFactoryImpl.getLog(PropertyUtil.class);

	public PropertyUtil() {
		super();
		setLogger(logger);
		setDelimiterParsingDisabled(true);
		Iterator<Entry<Object, Object>> iterator = System.getProperties().entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Object, Object> entry = iterator.next();
			String skey = String.valueOf(entry.getKey());
			String sval = String.valueOf(entry.getValue());
			if (!StringMatcher.like("^(sun\\.|java\\.).*").match(skey)) {
				Object[] vals = sval != null && sval.indexOf(getListDelimiter()) >= 0
						? sval.split(getListDelimiter() + "") : new Object[] { sval };
				for (Object val : vals) {
					super.addPropertyDirect(skey, val);
				}
			}
		}
	}

	@Override
	protected void addPropertyDirect(String key, Object value) {
		if (!System.getProperties().containsKey(key)) {
			if (key.toLowerCase().startsWith("system.")) {
				super.addPropertyDirect(key, value);
				key = key.substring(key.indexOf(".") + 1);
				System.setProperty(key, (String) value);
			}
			super.addPropertyDirect(key, value);
		} else {
			String sysVal = System.getProperty(key);
			if (!sysVal.equalsIgnoreCase(value.toString()))
				logger.debug("property [" + key + "] value [" + value
						+ "] ignored! It is overriden with System provided value: [" + sysVal + "]");
		}
	}

	public PropertyUtil(PropertyUtil prop) {
		this();
		append(prop);

	}

	public PropertyUtil(String... file) {
		this();
		load(file);
	}

	public void addAll(Map<String, ?> props) {
		boolean b = props.keySet().removeAll(System.getProperties().keySet());
		if(b){
			logger.debug("Found one or more system properties which will not modified");
		}
		copy(new MapConfiguration(props));
	}

	public PropertyUtil(File... file) {
		this();
		load(file);
	}

	public boolean load(String... files) {
		boolean r = true;
		for (String file : files) {

			file = getSubstitutor().replace(file);
			loadFile(new File(file));

		}
		return r;
	}

	public boolean load(File... files) {
		boolean r = true;
		for (File file : files) {
			loadFile(file);
		}
		return r;
	}

	private boolean loadFile(File file) {
		try {
			if (file.getName().endsWith("xml") || file.getName().contains(".xml.")) {
				load(new FileInputStream(file));
			} else {
				PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
				propertiesConfiguration.setEncoding(getString(ApplicationProperties.LOCALE_CHAR_ENCODING.getKey(), "UTF-8"));
				propertiesConfiguration.load(new FileInputStream(file));
				copy(propertiesConfiguration);
				propertiesConfiguration.clear();
			}
			return true;
		} catch (ConfigurationException e) {
			logger.error(e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}

		return false;
	}

	/**
	 * load property inside java/jar package
	 * 
	 * @param cls
	 * @param propertyFile
	 * @return
	 */
	public boolean load(Class<?> cls, String propertyFile) {
		boolean success = false;
		InputStream in = null;
		try {
			in = cls.getResourceAsStream(propertyFile);
			load(in);
			success = true;
		} catch (Exception e) {
			logger.error("Unable to load properties from file:" + propertyFile, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return success;
	}

	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public String[] getStringArray(String key, String... defaultValue) {
		String[] retVal = super.getStringArray(key);
		return (retVal != null) && (retVal.length > 0) ? retVal : defaultValue == null ? new String[] {} : defaultValue;
	}


	public Object getObject(String key) {
		return super.getProperty(key);
	}

	/**
	 * @param sPropertyName
	 * @return property-key value if key presents or key otherwise.
	 */
	public String getPropertyValue(String sPropertyName) {
		return getString(sPropertyName, sPropertyName);
	}

	/**
	 * @param sPropertyName
	 * @return property-key value if key presents or null otherwise
	 */
	public String getPropertyValueOrNull(String sPropertyName) {
		return getString(sPropertyName);
	}

	public void storePropertyFile(File f) {
		try {
			save(f);
		} catch (ConfigurationException e) {
			logger.error(e.getMessage());
		}
	}

	// don't add but overwrite
	/**
	 * this will overwrite existing value if any
	 */
	@Override
	public void addProperty(String key, Object value) {
		clearProperty(key);
		super.addProperty(key, value);
	}

	@Override
	public void setProperty(String key, Object value) {
		//allow List Delimiter for string value
		if(null!=value && value instanceof String){
			value = PropertyConverter.split(value.toString(),getListDelimiter());
		}
		super.setProperty(key, value);
	}
	/**
	 * Add a property to the configuration. If it already exists then the value
	 * stated here will be added to the configuration entry. For example, if the
	 * property: resource.loader = file is already present in the configuration
	 * and you call addProperty("resource.loader", "classpath") Then you will
	 * end up with a List like the following: ["file", "classpath"] Specified
	 * by: addProperty(...) in Configuration Parameters: key The key to add the
	 * property
	 * 
	 * @param key
	 * @param value
	 */
	public void editProperty(String key, Object value) {
		super.addProperty(key, value);
	}

	// clear property if it is not system property
	@Override
	public void clearProperty(String key) {
		if (!System.getProperties().containsKey(key)) {
			super.clearProperty(key);
		} else {
			logger.debug("clear system property ignored:" + key);
		}
	}
}
