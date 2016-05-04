package au.org.theark.arkcalendar.pages.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.collections.MultiMap;

import au.org.theark.arkcalendar.SamplePage;
import au.org.theark.arkcalendar.component.ArkMenuItem;
import au.org.theark.arkcalendar.dao.ArkCalendarDao;
import au.org.theark.arkcalendar.data.ArkCalendarVo;
import au.org.theark.arkcalendar.pages.calendar.ArkCalendarPage;
import au.org.theark.arkcalendar.util.SignIn2Session;

import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.widget.menu.IMenuItem;
import com.googlecode.wicket.jquery.ui.widget.menu.Menu;

public class DashBoardPage extends SamplePage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static MultiMap<String, ArkCalendarVo> studyCalendarMap = new MultiMap<String, ArkCalendarVo>();

	public DashBoardPage() {
		
		SignIn2Session session =(SignIn2Session)getSession();
		
		String user = session.getUser();
		this.add(new Menu("menu", DashBoardPage.newMenuItemList(user)) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target, IMenuItem item) {
				ArkMenuItem arkItem=(ArkMenuItem)item;

				getSession().setAttribute("calendarId", arkItem.getCalendarVo().getCalId());
				getSession().setAttribute("study", arkItem.getCalendarVo().getStudy());
				getSession().setAttribute("calendarName", arkItem.getCalendarVo().getCalName());
				
				setResponsePage(new ArkCalendarPage());
				

			}
		});

	}

	static List<IMenuItem> newMenuItemList(String user) {

		ArkCalendarDao calDao = new ArkCalendarDao();

		List<ArkCalendarVo> cals = calDao.getArkStudyCalendarList(user);
		
		studyCalendarMap.clear();

		for (ArkCalendarVo vo : cals) {
			studyCalendarMap.addValue(vo.getStudy(), vo);
		}

		List<IMenuItem> list = new ArrayList<IMenuItem>();

		Set<String> studySet = studyCalendarMap.keySet();

		for (String study : studySet) {
			ArkMenuItem studyItem = new ArkMenuItem(study, JQueryIcon.FLAG);
			studyItem.setEnabled(false);
			list.add(studyItem);

			List<ArkCalendarVo> calList = studyCalendarMap.get(study);

			for (ArkCalendarVo cal : calList) {
				ArkMenuItem calItem = new ArkMenuItem(cal.getCalName(),cal);
				
				list.add(calItem);
			}
		}

		return list;
	}

}
