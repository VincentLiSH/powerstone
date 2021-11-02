package org.powerstone.smartpagination.tag;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.powerstone.smartpagination.hdiv.HdivUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class HdivPagingHead extends PagingHead {
	private static final long serialVersionUID = 9114274801582407739L;

	public HdivPagingHead() {
		super();
		super.setEnableHdiv(true);
	}
	
	@Override
	protected String hdivEncodeUrl(String url, ServletRequest request, ServletResponse response) {
		if (!this.isEnableHdiv()) {
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
