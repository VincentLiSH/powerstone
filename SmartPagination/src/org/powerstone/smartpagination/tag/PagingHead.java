package org.powerstone.smartpagination.tag;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.powerstone.smartpagination.common.BasePagingController;
import org.powerstone.smartpagination.common.PageModel;

public class PagingHead extends TagSupport {
	private static final long serialVersionUID = -887745989108474554L;

	protected Logger log = Logger.getLogger(this.getClass());

	private String url;

	private String orderBy;

	private String styleClass;

	private String margin = "2%";

	private String align = "left";

	private boolean enableHdiv;

	@Override
	public int doStartTag() throws JspException {
		PageModel pm = (PageModel) pageContext.getRequest().getAttribute(
				BasePagingController.DEFAULT_PAGE_MODEL_NAME);

		if (pm == null) {
			log.warn("There is no PageModel in request:"
					+ ((HttpServletRequest) pageContext.getRequest()).getRequestURI());
			pm = new PageModel();
		}
		// write out
		try {
			String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
			String html = genHtml(pm, contextPath);
			pageContext.getOut().println(html.substring(0, html.indexOf("</label>")));
		} catch (IOException ioe) {
			throw new JspException(ioe);
		}

		return TagSupport.EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		PageModel pm = (PageModel) pageContext.getRequest().getAttribute(
				BasePagingController.DEFAULT_PAGE_MODEL_NAME);
		if (pm == null) {
			log.warn("There is no PageModel in request:"
					+ ((HttpServletRequest) pageContext.getRequest()).getRequestURI());
			pm = new PageModel();
		}
		// write out
		try {
			String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
			String html = genHtml(pm, contextPath);
			pageContext.getOut().println(html.substring(html.indexOf("</label>")));
		} catch (IOException ioe) {
			throw new JspException(ioe);
		}

		return EVAL_PAGE;
	}

	protected String genHtml(PageModel pm, String contextPath) {
		ResourceBundle rb = ResourceBundle.getBundle("paging_messages");
		if (url.indexOf("?") < 0) {
			url += "?";
		}

		String currDir = PageModel.ORDER_ASC;
		String currImg = PageModel.ORDER_ASC;
		String style;
		String tdStyle = "";
		String adjust = "";
		String imgUp;
		String imgDown;

		if (orderBy.equals(pm.getOrderBy())) {
			currDir = (PageModel.ORDER_ASC.equals(pm.getOrderDirection())) ? PageModel.ORDER_DESC
					: PageModel.ORDER_ASC;
			currImg = (PageModel.ORDER_ASC.equals(pm.getOrderDirection())) ? PageModel.ORDER_ASC
					: PageModel.ORDER_DESC;
		} else {
			currImg = "NULL";
		}
		String fullUrl = contextPath + url + "&" + BasePagingController.PAGE_SIZE_PARAM + "="
				+ pm.getPageSize() + "&" + BasePagingController.TO_PAGE_NO_PARAM + "="
				+ pm.computeNewPageNoInTag() + "&" + BasePagingController.ORDER_BY_PARAM + "="
				+ orderBy + "&" + BasePagingController.ORDER_DIR_PARAM + "=" + currDir;

		if (styleClass == null || styleClass.length() == 0) {
			style = "style=\"font-size:12px;line-height:22px;color: #FFFFFF;"
					+ "background-color: #0E8CBF;font-weight: bold;cursor:hand;\" ";
			tdStyle = "style=\"border-top-style:none;border-right-style: none;"
					+ "border-bottom-style: none;border-left-style: none;\" ";
			imgUp = "><span style=\"color:#660066\">&nbsp;∧&nbsp;</span>";
			imgDown = "><span style=\"color:#660066\">&nbsp;∨&nbsp;</span>";

		} else {
			style = "class=\"" + styleClass + "\" ";
			imgUp = "class=\"" + styleClass + "-up\">";
			imgDown = "class=\"" + styleClass + "-down\">";
			adjust = "<td>&nbsp;&nbsp;</td>\n\t";
		}

		StringBuffer htmlBuff = new StringBuffer();
		htmlBuff.append("<table title=\"");
		htmlBuff.append(rb.getString("pageHeadAlt"));
		htmlBuff.append("\" width=\"100%\" ");
		htmlBuff.append("cellpadding=\"0\" cellspacing=\"0\" ");
		htmlBuff.append(style);
		htmlBuff.append(" onclick=\"{location='");
		htmlBuff.append(this.hdivEncodeUrl(fullUrl, pageContext.getRequest(), pageContext
				.getResponse()));
		htmlBuff.append("'}\">\n<tr>\n\t");

		// 左边空白
		htmlBuff.append("<td ");
		htmlBuff.append(tdStyle);
		htmlBuff.append(" width=\"");
		htmlBuff.append(margin);
		htmlBuff.append("\"></td>\n\t");

		if (align.equals("center")) {
			htmlBuff.append(adjust);
			htmlBuff.append(adjust);
		}

		// 标题" >
		htmlBuff.append("<td ");
		if (adjust.length() == 0) {
			htmlBuff.append("align=\"");
			htmlBuff.append(align);
			htmlBuff.append("\" ");
		}
		htmlBuff.append(tdStyle);
		htmlBuff.append("> ");

		if (align.equals("left") || align.equals("center")) {
			htmlBuff.append("<label style=\"float:left\" id=\"coltitle\">");
			htmlBuff.append("</label>");
			htmlBuff.append("<div style=\"float:left\" ");
			if (currImg.equals(PageModel.ORDER_DESC)) {
				htmlBuff.append(imgDown);
			} else if (currImg.equals(PageModel.ORDER_ASC)) {
				htmlBuff.append(imgUp);
			} else {
				htmlBuff.append(">");
			}
			htmlBuff.append("</div>");
		} else if (align.equals("right")) {
			htmlBuff.append("<div style=\"float:right\"");
			if (currImg.equals(PageModel.ORDER_DESC)) {
				htmlBuff.append(imgDown);
			} else if (currImg.equals(PageModel.ORDER_ASC)) {
				htmlBuff.append(imgUp);
			}
			htmlBuff.append("</div>");
			htmlBuff.append("<label style=\"float:right\" id=\"coltitle\">");
			htmlBuff.append("</label>");
		}
		htmlBuff.append("</td>\n\t");

		if (align.equals("center")) {
			htmlBuff.append(adjust);
		}

		// 右边空白
		htmlBuff.append("<td  ");
		htmlBuff.append(tdStyle);
		htmlBuff.append(" width=\"");
		htmlBuff.append(margin);
		htmlBuff.append("\"></td>\n");

		htmlBuff.append("</tr>\n</table>\n");

		String barHtml = htmlBuff.toString();
		log.debug(barHtml);
		return barHtml;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isEnableHdiv() {
		return enableHdiv;
	}

	public void setEnableHdiv(boolean enableHdiv) {
		this.enableHdiv = enableHdiv;
	}

	protected String hdivEncodeUrl(String url, ServletRequest request, ServletResponse response) {
		return url;
	}
}
