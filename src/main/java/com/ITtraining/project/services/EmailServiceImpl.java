package com.ITtraining.project.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ITtraining.project.entities.VoucherEntity;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Override
	public void sendTemplateMessage(VoucherEntity voucher) throws Exception {

		MimeMessage mail = emailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(voucher.getVoucherUser().getEmail());
		helper.setSubject("YOUR VOUCHER");
		
		String text = "<html>" + 
							"<body>" + 
								"<table style='border:2px solid black'>" + 
									"<tr>" + 
										"<th>Buyer</th> <th>Offer</th> <th>Price</th> <th>Expires date</th> " +
									"</tr>" + 
									"<tr>" +
										"<td>" + voucher.getVoucherUser().getFirstName() + " " + voucher.getVoucherUser().getLastName() + "</td>" +
										"<td>" + voucher.getVoucherOffer().getOfferName() + "</td>" + 
										"<td>" + voucher.getVoucherOffer().getActionPrice() + "</td>" + 
										"<td>" + voucher.getExpirationDate().toString() + "</td>" + 
									"</tr>" +
								"</table>" +
							"</body>" +
						"</html>";
		
		helper.setText(text, true);

		emailSender.send(mail);

	}
}
