package org.powerstone.smartpagination.tag;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hdiv.dataComposer.IDataComposer;
import org.hdiv.util.HDIVUtil;
import org.powerstone.smartpagination.hdiv.HdivUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class HdivPagingBar extends PagingBar {

	private static final long serialVersionUID = 3586765351015746943L;
	
	public HdivPagingBar() {
		super();
		super.setEnableHdiv(true);
	}

	@Override
	protected String generateHiddenForForm(String url, String[] formInputs) {

		IDataComposer dataComposer = (IDataComposer) this.pageContext.getRequest().getAttribute(
				"dataComposer");
		if (dataComposer != null) {
			if (url.contains("?")) {
				url = url.substring(0, url.indexOf("?"));
			}
			dataComposer.beginRequest(HDIVUtil.getActionMappingName(url));
			if (formInputs != null && formInputs.length > 0) {
				for (String input : formInputs) {
					dataComposer.compose(input, "", true, "text");
				}
			}
			return dataComposer.endRequest();
		} else {
			log.warn("no dataComposer in session!!!!!!!!!!!!!!!");
			return null;
		}
	}

	@Override
	protected String hdivEncodeUrl(String url, ServletRequest request, ServletResponse response) {
		if (!this.getEnableHdiv()) {
			return url;
		} else {
			WebApplicationContext wac = WebApplicationContextUtils
					.getRequiredWebApplicationContext(pageContext.getServletContext());
			String[] beanNamesForType = wac.getBeanNamesForType(HdivUtil.class);
			if (beanNamesForType != null && beanNamesForType.length > 0) {
				HdivUtil util = (HdivUtil) wac.getBean(beanNamesForType[0]);
				return util.encodeUrl(url, (HttpServletRequest) request,
						(HttpServletResponse) response);
			} else {
				log.warn("enableHdiv is true,but no HdivUtil exists!!!!!!!!!!!!!!!");
				return url;
			}
		}
	}
}
