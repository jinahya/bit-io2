package com.github.jinahya.bit.io;

class UserListAdapter extends ListValueAdapter<User> {

    public UserListAdapter(int lengthSize) {
        super(lengthSize, new UserAdapter());
    }
}
