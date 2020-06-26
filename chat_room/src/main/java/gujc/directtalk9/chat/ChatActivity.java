package gujc.directtalk9.chat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import gujc.directtalk9.R;
import gujc.directtalk9.fragment.ChatFragment;
import gujc.directtalk9.fragment.UserListInRoomFragment;
import gujc.directtalk9.model.ChatModel;

public class ChatActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ChatFragment chatFragment;
    private UserListInRoomFragment userListInRoomFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        String toUid = getIntent().getStringExtra("toUid");
        final String roomID = getIntent().getStringExtra("roomID");
        String roomTitle = getIntent().getStringExtra("roomTitle");

        // chatting area
        chatFragment = ChatFragment.getInstance(toUid, roomID);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, chatFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.exit, R.anim.exit_anim);
        chatFragment.backPressed();
    }
}
