package j3.footpon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import j3.footpon.model.ImageCache;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView;

public class StoreListActivity extends ExpandableListActivity {
	MySimpleExpandableListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        getExpandableListView().setBackgroundResource(R.drawable.lvselectedbackground);
        // Set up our adapter
        mAdapter = new MySimpleExpandableListAdapter(this, groupData,
                R.layout.store_list,
                new String[] { NAME, LOGO }, new int[] { R.id.ItemTitle,
        		R.id.last }, childData, R.layout.list_coupon, 
        		new String[] { TYPE }, new int[] { R.id.type });
        setListAdapter(mAdapter);
        
        getExpandableListView().setOnItemSelectedListener(
        		new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int position, long id) {
                        System.out.println(position);
                        // mAdapter.notifyDataSetChanged();
                        View v = getExpandableListView().getChildAt(position);
                        if (v instanceof RelativeLayout) {
                            RelativeLayout r = (RelativeLayout) v;
                            ImageView image = (ImageView) r
                                    .findViewById(R.id.ItemImage);
                            image.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        
        getExpandableListView().setOnChildClickListener(
        		new ExpandableListView.OnChildClickListener(){
        			@Override
        			public boolean onChildClick(ExpandableListView parent, 
        					View v, int groupPosition, int childPosition, long id) {
        				// get parent data
//        				ExpandableListAdapter adapter = parent.getExpandableListAdapter();

        				// get child data
//        				Map<String, Object> childMap = (Map<String, Object>)adapter.getChild(
//        						groupPosition, childPosition);
        				
        				Intent myIntent = new Intent(StoreListActivity.this, Coupon.class);
        				startActivity(myIntent);
        				
        				return false;
        			}
        		});
    }
    
    private static final String NAME = "storename";
    private static final String LOGO = "storelogo";
    private static final String TYPE = "coupontype";
    private List<Map<String, Object>> groupData = new ArrayList<Map<String, Object>>();
    private List<List<Map<String, Object>>> childData = new ArrayList<List<Map<String, Object>>>();

    private void loadData() {
        for (int i = 0; i < 4; i++) {
            Map<String, Object> curGroupMap = new HashMap<String, Object>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, "Store " + i);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.puma);
            try {
                ImageCache.setBitmap("LOGO " + i, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            curGroupMap.put(LOGO, "LOGO " + i);

            List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();

            Map<String, Object> curChildMap1 = new HashMap<String, Object>();
            curChildMap1.put(TYPE, "Save 10$");
            children.add(curChildMap1);
            Map<String, Object> curChildMap2 = new HashMap<String, Object>();
            curChildMap2.put(TYPE, "buy one and get one free");
            children.add(curChildMap2);
            Map<String, Object> curChildMap3 = new HashMap<String, Object>();
            curChildMap3.put(TYPE, "10% off");
            children.add(curChildMap3);
            childData.add(children);
        }
    }
    
    public class MySimpleExpandableListAdapter extends

		    BaseExpandableListAdapter {
		
		private List<? extends Map<String, ?>> mGroupData;
		private int mExpandedGroupLayout;
		private int mCollapsedGroupLayout;
		private String[] mGroupFrom;
		private int[] mGroupTo;
		
		private List<? extends List<? extends Map<String, ?>>> mChildData;
		private int mChildLayout;
		private int mLastChildLayout;
		private String[] mChildFrom;
		private int[] mChildTo;
		
		private LayoutInflater mInflater;
		
		public MySimpleExpandableListAdapter(Context context,
		        List<? extends Map<String, ?>> groupData, int groupLayout,
		        String[] groupFrom, int[] groupTo,
		        List<? extends List<? extends Map<String, ?>>> childData,
		        int childLayout, String[] childFrom, int[] childTo) {
		    this(context, groupData, groupLayout, groupLayout, groupFrom,
		            groupTo, childData, childLayout, childLayout, childFrom,
		            childTo);
		}
		
		public MySimpleExpandableListAdapter(Context context,
		        List<? extends Map<String, ?>> groupData,
		        int expandedGroupLayout, int collapsedGroupLayout,
		        String[] groupFrom, int[] groupTo,
		        List<? extends List<? extends Map<String, ?>>> childData,
		        int childLayout, String[] childFrom, int[] childTo) {
		    this(context, groupData, expandedGroupLayout, collapsedGroupLayout,
		            groupFrom, groupTo, childData, childLayout, childLayout,
		            childFrom, childTo);
		}
		
		public MySimpleExpandableListAdapter(Context context,
		        List<? extends Map<String, ?>> groupData,
		        int expandedGroupLayout, int collapsedGroupLayout,
		        String[] groupFrom, int[] groupTo,
		        List<? extends List<? extends Map<String, ?>>> childData,
		        int childLayout, int lastChildLayout, String[] childFrom,
		        int[] childTo) {
		    mGroupData = groupData;
		    mExpandedGroupLayout = expandedGroupLayout;
		    mCollapsedGroupLayout = collapsedGroupLayout;
		    mGroupFrom = groupFrom;
		    mGroupTo = groupTo;
		
		    mChildData = childData;
		    mChildLayout = childLayout;
		    mLastChildLayout = lastChildLayout;
		    mChildFrom = childFrom;
		    mChildTo = childTo;
		
		    mInflater = (LayoutInflater) context
		            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public Object getChild(int groupPosition, int childPosition) {
		    return mChildData.get(groupPosition).get(childPosition);
		}
		
		public long getChildId(int groupPosition, int childPosition) {
		    return childPosition;
		}
		
		public View getChildView(int groupPosition, int childPosition,
		        boolean isLastChild, View convertView, ViewGroup parent) {
		    View v;
		    if (convertView == null) {
		        v = newChildView(isLastChild, parent);
		    } else {
		        v = convertView;
		    }
		    bindView(v, mChildData.get(groupPosition).get(childPosition),
		            mChildFrom, mChildTo);
		    v.setId(groupPosition * 100 + childPosition);
		    return v;
		}
		
		public View newChildView(boolean isLastChild, ViewGroup parent) {
		    return mInflater.inflate((isLastChild) ? mLastChildLayout
		            : mChildLayout, parent, false);
		}
		
		private void bindView(View view, Map<String, ?> data, String[] from,
		        int[] to) {
		    int len = to.length;
		    Object value = null;
		    for (int i = 0; i < len; i++) {
		        View v = view.findViewById(to[i]);
		        value = data.get(from[i]);
		
		        if (v instanceof Checkable) {
		            if (value instanceof Boolean) {
		                ((Checkable) v).setChecked((Boolean) value);
		            } else {
		                throw new IllegalStateException(v.getClass().getName()
		                        + " should be bound to a Boolean, not a "
		                        + data.getClass());
		            }
		        } else if (v instanceof TextView) {
		            ((TextView) v).setText(value.toString());
		        } else if (v instanceof ImageView) {
		            if (value != null && !value.equals("")) {
		                if (value instanceof String) {
		                    // ((ImageView) v).setImageResource((Integer)
		                    // value);
		                    Bitmap bm = null;
		                    try {
		                        bm = ImageCache.getBitmap((String) value);
		                    } catch (Exception e) {
		                        e.printStackTrace();
		                    }
		                    ((ImageView) v).setImageBitmap(bm);
		                } else {
		                    //
		                }
		            }
		        }
		    }
		}
		
		public int getChildrenCount(int groupPosition) {
		    return mChildData.get(groupPosition).size();
		}
		
		public Object getGroup(int groupPosition) {
		    return mGroupData.get(groupPosition);
		}
		
		public int getGroupCount() {
		    return mGroupData.size();
		}
		
		public long getGroupId(int groupPosition) {
		    return groupPosition;
		}
		
		public View getGroupView(int groupPosition, boolean isExpanded,
		        View convertView, ViewGroup parent) {
		    View v;
		    if (convertView == null) {
		        v = newGroupView(isExpanded, parent);
		    } else {
		        v = convertView;
		    }
		    bindView(v, mGroupData.get(groupPosition), mGroupFrom, mGroupTo);
		    return v;
		}
		
		public View newGroupView(boolean isExpanded, ViewGroup parent) {
		    return mInflater.inflate((isExpanded) ? mExpandedGroupLayout
		            : mCollapsedGroupLayout, parent, false);
		}
		
		public boolean isChildSelectable(int groupPosition, int childPosition) {
		    return true;
		}
		
		public boolean hasStableIds() {
		    return true;
		}
    }

}
