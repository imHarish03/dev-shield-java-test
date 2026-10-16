package demo.devshield.admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Operations for administrators, each with a classic mistake. */
@RestController
public class AdminController {

	private static final Path REPORTS = Path.of("reports");

	/** Checks that a host answers, by running ping with the caller's text. */
	@GetMapping("/admin/ping")
	public String ping(@RequestParam String host) throws IOException {
		// Command injection: "example.com; rm -rf /" runs both.
		Process process = Runtime.getRuntime().exec("ping -c 1 " + host);
		return new String(process.getInputStream().readAllBytes());
	}

	/** Downloads a report by name. */
	@GetMapping("/admin/reports")
	public byte[] report(@RequestParam String name) throws IOException {
		// Path traversal: "../../etc/passwd" leaves the reports folder.
		return Files.readAllBytes(REPORTS.resolve(name));
	}

	/** Imports accounts from an XML file. */
	@PostMapping("/admin/import")
	public String importXml(InputStream xml) throws Exception {
		// XXE: external entities are not switched off.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		return factory.newDocumentBuilder().parse(xml).getDocumentElement().getNodeName();
	}

	/** Restores saved settings from a Java-serialized upload. */
	@PostMapping("/admin/restore")
	public String restore(@RequestBody byte[] saved) throws Exception {
		try (ObjectInputStream in = new ObjectInputStream(new java.io.ByteArrayInputStream(saved))) {
			// Unsafe deserialization of whatever the caller sends.
			return String.valueOf(in.readObject());
		}
	}

	/** Encrypts an export with DES, which is broken. */
	@PostMapping("/admin/export")
	public byte[] export(@RequestBody byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, javax.crypto.KeyGenerator.getInstance("DES").generateKey());
		return cipher.doFinal(data);
	}

	/** Sends the browser on after sign-in, wherever the link says. */
	@GetMapping("/admin/continue")
	public String continueTo(@RequestParam String next) {
		return "redirect:" + next;
	}

}
