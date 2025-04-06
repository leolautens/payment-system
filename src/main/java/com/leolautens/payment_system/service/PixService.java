package com.leolautens.payment_system.service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;

import com.leolautens.payment_system.dto.PixChargeRequest;
import com.leolautens.payment_system.pix.Credentials;
import org.json.JSONArray;
import org.json.JSONObject;


import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;

@Service

public class PixService {

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;


    public JSONObject pixCreateEvp() {

        JSONObject options = recoverJsonObject();

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

        JSONObject options = this.recoverJsonObject();

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

        JSONObject options = recoverJsonObject();
        String randomTxid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("txid", randomTxid);

        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 900));
        //body.put("devedor", new JSONObject().put("cpf", "12345678909").put("nome", "Francisco da Silva"));
        body.put("valor", new JSONObject().put("original", pixChargeRequest.value()));
        body.put("chave", pixChargeRequest.key());

//        JSONArray infoAdicionais = new JSONArray();
//        infoAdicionais.put(new JSONObject().put("nome", "Campo 1").put("valor", "Informação Adicional1 do PSP-Recebedor"));
//        infoAdicionais.put(new JSONObject().put("nome", "Campo 2").put("valor", "Informação Adicional2 do PSP-Recebedor"));
//        body.put("infoAdicionais", infoAdicionais);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixCreateCharge", params, body);

            int idFromJson = response.getJSONObject("loc").getInt("id");
            generateQRCode(String.valueOf(idFromJson));

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

    private void generateQRCode(String id) {

        JSONObject options = recoverJsonObject();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixGenerateQRCode", params, new JSONObject());

            System.out.println(response);

            File outputfile = new File("qrCodeImage.png");
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(javax.xml.bind.DatatypeConverter
                    .parseBase64Binary(((String) response.get("imagemQrcode"))
                            .split(",")[1]))), "png", outputfile);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(outputfile);

        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private JSONObject recoverJsonObject() {

        Credentials credentials = new Credentials();
        JSONObject options = new JSONObject();
        options.put("client_id", clientId);
        options.put("client_secret", clientSecret);
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.isSandbox());

        return options;
    }
}
