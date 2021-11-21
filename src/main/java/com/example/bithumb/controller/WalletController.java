package com.example.bithumb.controller;



import com.example.bithumb.dto.InvestForm;
import com.example.bithumb.dto.WalletForm;
import com.example.bithumb.dto.WalletResponse;
import com.example.bithumb.service.BitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WalletController {
    private final BitService bitService;

    //todo rest api -> thymeleaf 적용밤면으로 변경 중
    @GetMapping("/create/wallet")
    public String walletForm(Model model) {
        model.addAttribute("walletForm", new WalletForm());
        return "wallet/create";
    }

    @PostMapping("/create/wallet")
    public String createWallet(@ModelAttribute WalletForm form) {
        bitService.createWallet(form);
        return "redirect:/index";
    }

    @GetMapping("/wallet/{id}")
    public String getWallet(@PathVariable Long id, Model model) {
        WalletResponse myWallet = bitService.getMyWallet(id);
        model.addAttribute("myWallet", myWallet);
        model.addAttribute("invest", new InvestForm());
        return "wallet/get";
    }

    @PostMapping("/invest/{id}")
    public String invest(@PathVariable Long id,@ModelAttribute InvestForm form) {
        WalletResponse res = bitService.invest(id,form);
        return "redirect:/wallet/get/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteWallet(@PathVariable Long id) {
        Long result = bitService.removeWallet(id);
        if (result == null) {
            return "error/error";
        }
        return "redirect:/";
    }

}
