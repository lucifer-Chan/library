package cn.ttsales;

import cn.ttsales.dao.WxTeacherRepository;
import cn.ttsales.domain.WxTeacher;
import cn.ttsales.util.Constant;
import cn.ttsales.util.CookieUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LibraryApplicationTests {
	@Autowired
	WxTeacherRepository wxTeacherRepository;

	MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

//	String expectedJson;

	private String obj2Json(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	@Before
	public void init(){
		WxTeacher t = new WxTeacher();
		t.setName("test1");
		t.setMobile("11111111111");
		wxTeacherRepository.save(t);
//		expectedJson = obj2Json(wxTeacherRepository.findAll());
		mvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testTeacherController() throws Exception{
		String uri = "/teacher/course";
		MockHttpServletResponse response =
				mvc.perform(get(uri).cookie(new Cookie(CookieUtil.KEY_USER_ID, "e165d04bf4394001a7a747e6497245ea")))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse();
		System.out.println(response.getContentAsString());
	}

//	@Test
//	public void testTeacherController() throws Exception{
//		String uri = "/teacher/course";
//		MvcResult ret = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
//		int status = ret.getResponse().getStatus();
//		String content = ret.getResponse().getContentAsString();
//		Assert.assertEquals("错误，正确值为200", 200, status);
//		Assert.assertEquals("错误，返回值和预期不一致", expectedJson, content);
//	}


//	@Test
//	public void contextLoads() {
//	}

}
