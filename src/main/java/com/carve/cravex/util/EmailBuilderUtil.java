package com.carve.cravex.util;

public class EmailBuilderUtil {

    public static String otpEmailTemplateBuilder(String name, String otp){
        return String.format("""
            <html>
            <body style="font-family: Arial; background-color: #f5f5f5; padding: 20px;">
                <div style="max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px;">
                    <h1 style="color: #ff6b35; text-align: center;">CraveX</h1>
                    <h2 style="color: #333;">Verify Your Email</h2>
                    <p>Hi <strong>%s</strong>,</p>
                    <p>Your OTP code is:</p>
                    <h2 style="background: #f0f0f0; padding: 20px; text-align: center; color: #ff6b35; letter-spacing: 5px;">%s</h2>
                    <p style="color: #666;">This code is valid for 10 minutes.</p>
                    <p style="color: #999; font-size: 12px;">© 2026 CraveX</p>
                </div>
            </body>
            </html>
            """, name, otp);
    }

    public static String welcomeEmailBuilder(String name){
        return String.format("""
<html>
<body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
    <div style="max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
        <h1 style="color: #ff6b35; text-align: center; margin-bottom: 10px;">
             CraveX
        </h1>
        <h2 style="color: #333; text-align: center;">
            Welcome to CraveX!
        </h2>
        <p>Hi <strong>%s</strong>,</p>
        <p>
            Thank you for joining <strong>CraveX</strong>. Your account has been
            successfully created and you're now ready to explore delicious meals
            from your favorite restaurants.
        </p>
        <div style="background-color: #fff4ef; border-left: 4px solid #ff6b35; padding: 15px; margin: 20px 0;">
            <p style="margin: 0;">
                 Welcome aboard! We’re excited to have you as part of the CraveX family.
            </p>
        </div>
        <p>With CraveX, you can:</p>
        <ul style="color: #555;">
            <li> Browse restaurants near you</li>
            <li> Order your favorite meals</li>
            <li> Track deliveries in real-time</li>
            <li> Enjoy exclusive offers and discounts</li>
        </ul>
        <p>
            Start exploring and satisfy your cravings today!
        </p>
        <p>
            Happy Ordering,<br>
            <strong>The CraveX Team</strong>
        </p>
        <hr style="border: none; border-top: 1px solid #eee; margin: 25px 0;">

        <p style="color: #999; font-size: 12px; text-align: center;">
            © 2026 CraveX. All Rights Reserved.
        </p>
    </div>
</body>
</html>
""", name);
    }

    public static String orderEmailTemplateBuilder(String name, String orderId, String totalAmount){
        return String.format("""
            <html>
            <body style="font-family: Arial; background-color: #f5f5f5; padding: 20px;">
                <div style="max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px;">
                    <h1 style="color: #ff6b35; text-align: center;">CraveX</h1>
                    <h2 style="color: #333;">Order Confirmed!</h2>
                    <p>Hi <strong>%s</strong>,</p>
                    <p>Your order has been confirmed!</p>
                    <div style="background: #f9f9f9; padding: 20px; border-radius: 5px;">
                        <p><strong>Order ID:</strong> %s</p>
                        <p><strong>Total Amount:</strong> ₹%s</p>
                        <p><strong>Estimated Delivery:</strong> 30-45 minutes</p>
                    </div>
                    <p style="color: #666;">Thank you for ordering with CraveX!</p>
                    <p style="color: #999; font-size: 12px;">© 2024 CraveX</p>
                </div>
            </body>
            </html>
            """, name, orderId, totalAmount);
    }

    public static String deliveryEmailTemplateBuilder(String name, String orderId){
        return String.format("""
            <html>
            <body style="font-family: Arial; background-color: #f5f5f5; padding: 20px;">
                <div style="max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px;">
                    <h1 style="color: #ff6b35; text-align: center;">CraveX</h1>
                    <h2 style="color: #4caf50;">Order Delivered! </h2>
                    <p>Hi <strong>%s</strong>,</p>
                    <p>Your order has been successfully delivered!</p>
                    <div style="background: #f9f9f9; padding: 20px; border-radius: 5px;">
                        <p><strong>Order ID:</strong> %s</p>
                        <p><strong>Status:</strong> ✓ Delivered</p>
                    </div>
                    <p style="color: #666;">Please rate your experience and share your feedback!</p>
                    <p style="color: #999; font-size: 12px;">© 2024 CraveX</p>
                </div>
            </body>
            </html>
            """, name, orderId);
    }
}
