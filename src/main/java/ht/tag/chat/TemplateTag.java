package ht.tag.chat;

import ht.domain.ChatMessage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.TagSupport;

@Getter
@Setter
@Slf4j
public class TemplateTag extends RequestContextAwareTag {

	private static final long serialVersionUID = 1L;

	private String jsonContents;
	// private WtfSpringBean wtfSpringBean;

	/**
	 * Called by doStartTag to perform the actual work.
	 *
	 * @return same as TagSupport.doStartTag
	 * @throws Exception any exception, any checked one other than
	 *                   a JspException gets wrapped in a JspException by doStartTag
	 * @see TagSupport#doStartTag
	 */
	@Override
	protected int doStartTagInternal() throws Exception {

		// WebApplicationContext ctx = getRequestContext().getWebApplicationContext();
		// wtfSpringBean = ctx.getBean(WTF.class);

		log.debug("jsonContents: {}", jsonContents);

		try {
			JspWriter out = pageContext.getOut();
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setCont(jsonContents);
			out.write(chatMessage.getMessages());
			log.debug(chatMessage.getMessages());
		} catch (Exception e) {
			log.error("{}, jsonContents: {}", e.getLocalizedMessage(), jsonContents);
			throw new SkipPageException(e.getLocalizedMessage());
		}

		return SKIP_BODY;
	}
}
