package com.leolautens.payment_system.service;

import java.util.HashMap;

import com.leolautens.payment_system.dto.PixChargeRequest;
import com.leolautens.payment_system.pix.Credentials;
import org.json.JSONArray;
import org.json.JSONObject;


import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service

public class PixService {

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;


    public JSONObject pixCreateEvp() {

        Credentials credentials = new Credentials();
        JSONObject options = recoverJsonObject(credentials);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixCreateEvp", new HashMap<String,String>(), new JSONObject());
            System.out.println(response);
            return response;
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public JSONObject listEVP() {
        Credentials credentials = new Credentials();

        JSONObject options = this.recoverJsonObject(credentials);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixListEvp", new HashMap<String,String>(), new JSONObject());
            System.out.println(response);
            return response;
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public JSONObject pixCreateCharge(PixChargeRequest pixChargeRequest) {

        Credentials credentials = new Credentials();
        JSONObject options = recoverJsonObject(credentials);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("txid", "2908c0c97ea847e78e8849634473c1f1");

        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 900));
        body.put("devedor", new JSONObject().put("cpf", "12345678909").put("nome", "Francisco da Silva"));
        body.put("valor", new JSONObject().put("original", pixChargeRequest.value()));
        body.put("chave", pixChargeRequest.key());

        JSONArray infoAdicionais = new JSONArray();
        infoAdicionais.put(new JSONObject().put("nome", "Campo 1").put("valor", "Informação Adicional1 do PSP-Recebedor"));
        infoAdicionais.put(new JSONObject().put("nome", "Campo 2").put("valor", "Informação Adicional2 do PSP-Recebedor"));
        body.put("infoAdicionais", infoAdicionais);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixCreateCharge", params, body);
            System.out.println(response);
            return response;
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private JSONObject recoverJsonObject(Credentials credentials) {
        JSONObject options = new JSONObject();
        options.put("client_id", clientId);
        options.put("client_secret", clientSecret);
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.isSandbox());

        return options;
    }
}
