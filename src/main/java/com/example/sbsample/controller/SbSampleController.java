package com.example.sbsample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SbSampleController {
    @RequestMapping("/sbsample")
	//public String index() {
	public String index(Model model) {
		model.addAttribute("msg", "Hello! Thymleaf!!");
		// 返却するのはhtmlのファイル名
		return "hello";
	}

    /**
     * /useredit パスへのリクエストを処理し、Vue.jsアプリケーションを
     * 読み込むためのビューを返します。
     * これにより、クライアントサイドのVue RouterがURLを解釈して
     * 適切なコンポーネント（UserEditForm.vue）を表示します。
     * @param model ビューに渡すモデル
     * @return ビュー名 (useredit.html)
     */
    @GetMapping("/useredit")
    public String useredit(Model model) {
        // 必要に応じて、ビューに渡すメッセージなどを設定できます
        model.addAttribute("msg", "ユーザー編集画面");
        return "useredit"; // Vueアプリがマウントされるuseredit.htmlを返す
    }

    /**
     * /useredit パスへのリクエストを処理し、Vue.jsアプリケーションを
     * 読み込むためのビューを返します。
     * これにより、クライアントサイドのVue RouterがURLを解釈して
     * 適切なコンポーネント（UserEditForm.vue）を表示します。
     * @param model ビューに渡すモデル
     * @return ビュー名 (useredit.html)
     */
    @GetMapping("/usersearch")
    public String usersearch(Model model) {
        // 必要に応じて、ビューに渡すメッセージなどを設定できます
        model.addAttribute("msg", "ユーザー検索画面");
        return "useredit"; // Vueアプリがマウントされるuseredit.htmlを返す
    }

	/*
	@RequestMapping(value = { "/", "/useredit" })
	@ResponseBody
	public ModelAndView useredit(ModelAndView mv) {
		// Vueアプリがマウントされるhello.htmlを返す
		ModelAndView mav = new ModelAndView("hello");
		mav.setViewName("UserEditForm"); // hello.htmlに埋め込むVueアプリを指定
		return mav;
	}
	 */
}