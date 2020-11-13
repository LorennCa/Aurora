package co.ias.teseo;

import org.junit.Test;

public class JWTTester {	
	@Test
	public void keyGeneration()
	{
		/*Key key = MacProvider.generateKey(SignatureAlgorithm.HS256);
		byte[] bytes = key.getEncoded();
		System.out.println("key: " + bytes);
		
		String compactJws = Jwts.builder()
			    .setSubject("Joe")
			    .compressWith(CompressionCodecs.DEFLATE)
			    .signWith(SignatureAlgorithm.HS256, key)
			    .compact();
		System.out.println("compactJws: " + compactJws);
		
		System.out.println(Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject().equals("Joe"));
		
		ObjectOutputStream oout = null;
		try {
			oout = new ObjectOutputStream(new FileOutputStream("authSee.jks"));
			oout.writeObject(key);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				oout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Key keyy;
		ObjectInputStream oin = null;
		try {
			oin = new ObjectInputStream(new FileInputStream("authSee.jks"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		  keyy = (Key) oin.readObject();
		  byte[] bytess = key.getEncoded();
		  System.out.println("keyy: " + bytess);
		  
		  String compactJwss = Jwts.builder()
				    .setSubject("Joe")
				    .compressWith(CompressionCodecs.DEFLATE)
				    .signWith(SignatureAlgorithm.HS256, keyy)
				    .compact();
			System.out.println("compactJwss: " + compactJwss);
			
			System.out.println(Jwts.parser().setSigningKey(keyy).parseClaimsJws(compactJws).getBody().getSubject().equals("Joe"));

		  
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		  try {
			oin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}*/
		
		String a = "1";
		System.out.println(Integer.parseInt(a));
	}
}