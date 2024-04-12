package com.k11.automation.coreframework.util;

import com.k11.automation.coreframework.util.fileUtil.FileUtil;
import com.k11.automation.selenium.baseclasses.ApplicationProperties;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.hamcrest.Matchers;

import java.io.File;
import java.util.*;

/**
 * Configuration manager class. Singleton with early initialization.
 * <p>
 * This class loads file provided by system property
 * <code>application.properties.file</code> (Default value is
 * "resources/application.properties"). Also loads all property files form
 * <code>test.props.dir</code>(default value is "resources") if
 * <code>resources.load.subdirs</code> flag is 1.
 * <p>

 */
public class ConfigurationManager {
	// early initialization
	static final Log log = LogFactoryImpl.getLog(ConfigurationManager.class);
	private static final ConfigurationManager INSTANCE = new ConfigurationManager();

	/**
	 * Private constructor, prevents instantiation from other classes
	 */
	private ConfigurationManager() {
		AbstractConfiguration.setDefaultListDelimiter(';');
	}

	public static ConfigurationManager getInstance() {
		return INSTANCE;
	}

	private static InheritableThreadLocal<PropertyUtil> LocalProps =
			new InheritableThreadLocal<PropertyUtil>() {
				@Override
				protected PropertyUtil initialValue() {
					PropertyUtil p = new PropertyUtil(
							System.getProperty("application.properties.file",
									"resources/application.properties"));

					String[] resources = p.getStringArray("env.resources", "resources");
					for (String resource : resources) {
						addBundle(p, resource);
					}
					ConfigurationListener cl = new PropertyConfigurationListener();
					p.addConfigurationListener(cl);
					return p;
				}

				@Override
				protected PropertyUtil childValue(PropertyUtil parentValue) {
					PropertyUtil cp = new PropertyUtil(parentValue);
					ConfigurationListener cl = new PropertyConfigurationListener();
					cp.addConfigurationListener(cl);
					return cp;
				}

			};

	/**
	 * To add local resources.
	 *
	 * @param fileOrDir
	 */
	public static void addBundle(String fileOrDir) {
		ConfigurationManager.addBundle(getBundle(), fileOrDir);
	}

	/**
	 * @param p
	 * @param fileOrDir
	 */
	private static void addBundle(PropertyUtil p, String fileOrDir) {
		String localResources = p.getString("local.resources",
				p.getString("env.local.resources", "resources"));
		fileOrDir = p.getSubstitutor().replace(fileOrDir);
		File resourceFile = new File(fileOrDir);
		/**
		 * will reload existing properties value(if any) if the last loaded
		 * dir/file is not the current one. case: suit-1 default, suit-2 :
		 * s2-local, suit-3: default Here after suit-2 you need to reload
		 * default.
		 */
		if (!localResources.equalsIgnoreCase(resourceFile.getAbsolutePath())) {
			p.addProperty("local.reasources", resourceFile.getAbsolutePath());
			if (resourceFile.exists()) {
				if (resourceFile.isDirectory()) {
					boolean loadSubDirs = p.getBoolean("resources.load.subdirs", true);
					File[] propFiles = FileUtil.listFilesAsArray(resourceFile,
							".properties", StringComparator.Suffix, loadSubDirs);
					log.info("Resource dir: " + resourceFile.getAbsolutePath()
							+ ". Found property files to load: " + propFiles.length);
					PropertyUtil p1 = new PropertyUtil();
					p1.load(propFiles);
					p.copy(p1);

					propFiles = FileUtil.listFilesAsArray(resourceFile, ".xml",
							StringComparator.Suffix, loadSubDirs);
					log.info("Resource dir: " + resourceFile.getAbsolutePath()
							+ ". Found property files to load: " + propFiles.length);

					p1 = new PropertyUtil();
					p1.load(propFiles);
					p.copy(p1);

				} else {
					try {
						if (fileOrDir.endsWith(".properties")
								|| fileOrDir.endsWith(".xml") ){
							p.load(new File[]{resourceFile});
						}
					} catch (Exception e) {
						log.error(
								"Unable to load " + resourceFile.getAbsolutePath() + "!",
								e);
					}
				}
			} else {
				log.error(resourceFile.getAbsolutePath() + " not exist!");
			}
		}
	}

	private static void addLocal(PropertyUtil p, String local, String fileOrDir) {
		String defaultLocal = p.getString(ApplicationProperties.DEFAULT_LOCALE.getKey(), "");//
		File resourceFile = new File(fileOrDir);
		/**
		 * will reload existing properties value(if any) if the last loaded
		 * dir/file is not the current one.
		 */
		boolean loadSubDirs = p.getBoolean("resources.load.subdirs", true);

		if (resourceFile.exists()) {
			PropertyUtil p1 = new PropertyUtil();
			p1.setEncoding(
					p.getString(ApplicationProperties.LOCALE_CHAR_ENCODING.getKey(), "UTF-8"));
			if (resourceFile.isDirectory()) {
				File[] propFiles = FileUtil.listFilesAsArray(resourceFile, "." + local,
						StringComparator.Suffix, loadSubDirs);
				p1.load(propFiles);

			} else {
				try {
					if (fileOrDir.endsWith(local)) {
						p1.load(fileOrDir);
					}
				} catch (Exception e) {
					log.error("Unable to load " + resourceFile.getAbsolutePath() + "!",
							e);
				}
			}
			if (local.equalsIgnoreCase(defaultLocal)) {
				p.copy(p1);
			} else {
				Iterator<?> keyIter = p1.getKeys();
				Configuration localSet = p.subset(local);
				while (keyIter.hasNext()) {
					String key = (String) keyIter.next();
					localSet.addProperty(key, p1.getObject(key));
				}
			}

		} else {
			log.error(resourceFile.getAbsolutePath() + " not exist!");
		}
	}

	public static void addAll(Map<String, ?> props) {
		ConfigurationManager.getBundle().addAll(props);
	}

	public static PropertyUtil getBundle() {
		return ConfigurationManager.LocalProps.get();
	}

    private static class PropertyConfigurationListener implements ConfigurationListener {
		String oldValue;

		@SuppressWarnings("unchecked")
		@Override
		public void configurationChanged(ConfigurationEvent event) {

			if ((event.getType() == AbstractConfiguration.EVENT_CLEAR_PROPERTY
					|| event.getType() == AbstractConfiguration.EVENT_SET_PROPERTY)
					&& event.isBeforeUpdate()) {
				oldValue = String.format("%s",
						getBundle().getObject(event.getPropertyName()));
			}

			if ((event.getType() == AbstractConfiguration.EVENT_ADD_PROPERTY
					|| event.getType() == AbstractConfiguration.EVENT_SET_PROPERTY)
					&& !event.isBeforeUpdate()) {
				String key = event.getPropertyName();
				Object value = event.getPropertyValue();
				if (null != oldValue && Matchers.equalTo(oldValue).matches(value)) {
					// do nothing
					return;
				}


				String[] bundles = null;

				// Resource loading
				if (key.equalsIgnoreCase("env.resources")) {

					if (event.getPropertyValue() instanceof ArrayList<?>) {
						ArrayList<String> bundlesArray =
								((ArrayList<String>) event.getPropertyValue());
						bundles = bundlesArray.toArray(new String[bundlesArray.size()]);
					} else {
						String resourcesBundle = (String) value;
						if (StringUtil.isNotBlank(resourcesBundle))
							bundles = resourcesBundle.split(String
									.valueOf(PropertyUtil.getDefaultListDelimiter()));
					}
					if (null != bundles && bundles.length > 0) {
						for (String res : bundles) {
							log.info("Adding resources from: " + res);
							ConfigurationManager.addBundle(res);
						}
					}
				}
				// Locale loading
				if (key.equalsIgnoreCase(ApplicationProperties.DEFAULT_LOCALE.getKey())) {
					String[] resources =
							getBundle().getStringArray("env.resources", "resources");
					for (String resource : resources) {
						String fileOrDir = getBundle().getSubstitutor().replace(resource);
						addLocal(getBundle(), (String) event.getPropertyValue(),
								fileOrDir);
					}
				}

			}
		}
	}

}
