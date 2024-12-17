package com.isep.certification.auth.exceptions;

import org.springframework.http.HttpStatus;

import com.isep.certification.users.models.enums.LoginMode;

public enum AuthErrors {

	USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "", "Un compte est déjà associé au numéro de téléphone renseigné"),
	INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "", "Numéro de téléphone invalide (Ex: 771112233)"),
	BLOCKED_ACCOUNT(HttpStatus.UNAUTHORIZED, "",
			"Votre compte est bloqué. Veuillez contacter le service client pour plus d'informations"),
	INACTIVE_ACCOUNT(HttpStatus.UNAUTHORIZED, "",
			"Votre compte est inactif, merci de vous connecter pour recevoir un code d'activiation"),
	INVALID_CREDIDENTIALS(HttpStatus.BAD_REQUEST, "", "Numéro de téléphone ou mot de passe invalide"),
	SUBSCRIPTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "", "Abonnement introuvable"),
	USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "", "Un compte avec ce numéro de téléphone est introuvable"),
	ACCOUNT_ALREADY_ACTIVE(HttpStatus.BAD_REQUEST, "", "Le compte avec et numéro de téléphone est déjà activé"),
	VERIFICATION_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "", "Nous n'arrivons pas à exécuter la vérification"),
	INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "", "Le code de vérification est invalide"),
	EXPIRED_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "", "Le code de vérification a expiré"),
	REQUEST_NOT_VALIDATED(HttpStatus.BAD_REQUEST, "", "La demande de changement de mot de passe n'est pas autorisée"),
	PASSWORD_NOT_IDENTICAL(HttpStatus.BAD_REQUEST, "", "Les mots de passe ne correspondent pas"),
	INVALID_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "", "Le mot de passe actuel est invalide"),
	SAME_PASSWORD(HttpStatus.BAD_REQUEST, "", "Le mot de passe doit être différent de l'ancien"),
	NO_SESSION(HttpStatus.UNAUTHORIZED, "", "Votre session a expiré ou n'est pas valide"),
	NO_SESSION_IMPLICIT(HttpStatus.UNAUTHORIZED, "", "Votre session a expiré ou n'est pas valide", LoginMode.IMPLICIT),
	USER_COUNTRY_INVALID(HttpStatus.BAD_REQUEST, "", "Le numéro de téléphone n'existe pas ou le pays est incorrect"),
	COUNTRY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "", "Ce pays n'est pas autorisé ou n'existe pas"),
	EMPTY_FIELDS(HttpStatus.BAD_REQUEST, "", "Merci de remplir les champs vides");

	public final HttpStatus status;
	public final String code;
	public final String reason;
	public final LoginMode loginMode;

	private  AuthErrors(HttpStatus status, String code, String reason) {
		this.status = status;
		this.code = code;
		this.reason = reason;
		this.loginMode = null;
	}

	private  AuthErrors(HttpStatus status, String code, String reason, LoginMode loginMode) {
		this.status = status;
		this.code = code;
		this.reason = reason;
		this.loginMode = loginMode;
	}

}
