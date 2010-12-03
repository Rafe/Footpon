package j3.footpon.model;

import j3.footpon.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;

public class IconHelper {
	public static Drawable getMapIcon(String type,Activity context){
		Drawable image;
		if(type.equals(Footpon.CATEGORY_FOOD)){
			image = context.getResources().getDrawable(R.drawable.icon_food);
        }else if(type.equals(Footpon.CATEGORY_OUTDOOR)){
        	image = context.getResources().getDrawable(R.drawable.icon_outdoor);
        }else if(type.equals(Footpon.CATEGORY_TOYS)){
        	image = context.getResources().getDrawable(R.drawable.icon_toys);
        }else if(type.equals(Footpon.CATEGORY_VIDEO_GAME)){
        	image = context.getResources().getDrawable(R.drawable.icon_games);
        }else{
        	image =context.getResources().getDrawable(R.drawable.mark);
        }
		boundCenterButtom(image);
		return image;
	}
	
	public static void boundCenterButtom(Drawable image){
		image.setBounds(-image.getIntrinsicWidth() /2, -image.getIntrinsicHeight(),
				image.getIntrinsicWidth()/2, 0);
	}
	
	public static Drawable getLogo(String storeName,Activity context){
		Drawable image;
		if(storeName.equals("McDonald's")){
			image = context.getResources().getDrawable(R.drawable.mcdonald);
        }else if(storeName.equals("Nintendo World Store")){
        	image = context.getResources().getDrawable(R.drawable.nintendo);
        }else if(storeName.equals("Wendy's")){
        	image = context.getResources().getDrawable(R.drawable.wendys);
        }else if(storeName.equals("Burger King")){
        	image = context.getResources().getDrawable(R.drawable.burgerking);
        }else{
        	image =context.getResources().getDrawable(R.drawable.mark);
        }
		return image;
	}
}
