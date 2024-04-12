package com.k11.automation.coreframework.dataproviderhelper;

import com.k11.automation.coreframework.annotations.K11DataProvider;
import org.json.simple.JSONObject;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.k11.automation.coreframework.util.ConfigurationManager.getBundle;
import static org.codehaus.plexus.util.StringUtils.isNotBlank;

public class DataProviderUtil {

	public static void setDataProvider(ITestAnnotation testAnnotation, Method method) {
		if ((null != method) && null != method.getParameterTypes() && (method.getParameterTypes().length > 0)) {
			String dataProvider = testAnnotation.getDataProvider();
			boolean hasDataProvider = isNotBlank(dataProvider);

			// other than k11 data provider
			if (hasDataProvider && !dataProvider.startsWith(K11DataProvider.NAME)) {
				// keep actual data-provider details with description
				Map<String, String> desc = new HashMap<String, String>();
				desc.put("description", testAnnotation.getDescription());
				desc.put("dataProvider", testAnnotation.getDataProvider());
				Class<?> dpClass = testAnnotation.getDataProviderClass();
				if (null != dpClass) {
					desc.put("dataProviderClass", dpClass.getName());
				}
				testAnnotation.setDescription(new JSONObject(desc).toString());
			}

			boolean globalParallelSetting = getBundle().getBoolean("global.datadriven.parallel", false);
			boolean parallel = getBundle().getBoolean(method.getName() + ".parallel", globalParallelSetting);
			dataProvider = parallel ? K11DataProvider.NAME_PARALLEL : K11DataProvider.NAME;

			testAnnotation.setDataProvider(dataProvider);
			testAnnotation.setDataProviderClass(K11DataProvider.params.valueOf("DATAPROVIDERCLASS").getClass());
		}
	}
}