package org.powerstone.smartpagination.hdiv;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.taglibs.standard.tag.common.core.ImportSupport;
import org.apache.taglibs.standard.tag.common.core.ParamSupport;
import org.hdiv.config.HDIVConfig;
import org.hdiv.util.StandardRequestUtilsHDIV;

public class HdivUtil {
	private HDIVConfig config;

	public String encodeUrl(String url, HttpServletRequest request, HttpServletResponse response) {
		url = new ParamSupport.ParamManager().aggregateParams(url);

		// if the URL is relative, rewrite it
		if (!ImportSupport.isAbsoluteUrl(url)) {
			url = response.encodeURL(url);
		}

		if (StandardRequestUtilsHDIV.hasActionOrServletExtension(url, config
				.getProtectedURLPatterns())) {
			url = StandardRequestUtilsHDIV.addHDIVParameterIfNecessary(request, url);
		}
		return url;
	}

	public HDIVConfig getConfig() {
		return config;
	}

	public void setConfig(HDIVConfig config) {
		this.config = config;
	}
}
