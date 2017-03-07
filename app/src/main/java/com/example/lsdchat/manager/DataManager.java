package com.example.lsdchat.manager;

<<<<<<< Temporary merge branch 1
import com.example.lsdchat.model.ContactsModel;
=======
import com.example.lsdchat.model.RealmMessage;
>>>>>>> Temporary merge branch 2
import com.example.lsdchat.model.User;
import com.example.lsdchat.model.UserQuick;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DataManager {
    private Realm mRealm;

    public DataManager() {
        mRealm = Realm.getDefaultInstance();
    }

    public boolean insertUser(User user) {
        try {
            deleteAllUserDb();
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(user);
            mRealm.commitTransaction();
        } catch (Exception ex) {
            mRealm.cancelTransaction();
            return false;
        }
        return true;
    }

    public Realm getRealm() {
        return mRealm;
    }

    public User getUser() {
        return mRealm.where(User.class).findFirst();
    }

    public void deleteAllUserDb() {
        RealmResults<User> realmResults = mRealm.where(User.class).findAll();
        if (!realmResults.isEmpty()) {
            mRealm.executeTransaction(realm -> realm.deleteAll());
        }
    }

    //handle messages
    public void insertRealmMessage(ItemMessage message) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(message));
    }

    public List<ItemMessage> retrieveMessagesByDialogId(String chatDialogId) {
        return mRealm.where(ItemMessage.class).equalTo(ItemMessage.CHAT_DIALOG_ID, chatDialogId).findAllSorted(ItemMessage.DATE_SENT, Sort.DESCENDING);
    }

    public void insertUserQuickToDB(LoginUser user) {

        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(user));
    }


    public List<LoginUser> getUsersQuickList(String sort) {
        switch (sort) {
            case ApiConstant.SORT_CREATE_AT:
                return mRealm.where(LoginUser.class).findAll();
            case ApiConstant.SORT_NAME_ACS:
                return mRealm.where(LoginUser.class).findAllSorted("fullName", Sort.ASCENDING);
            case ApiConstant.SORT_NAME_DESC:
                return mRealm.where(LoginUser.class).findAllSorted("fullName", Sort.DESCENDING);
            default:
                return mRealm.where(LoginUser.class).findAll();
        }

    }

    public List<LoginUser> getUsersQuickList() {
        return mRealm.where(LoginUser.class).findAll();
    }

    public LoginUser getUserById(int id) {
        return mRealm.where(LoginUser.class).equalTo("id", id).findFirst();
    }

    public RealmResults<LoginUser> getUsersQuick() {
        return mRealm.where(LoginUser.class).findAll();
//        return mRealm.where(LoginUser.class).findAllSorted("fullName", Sort.ASCENDING);
    }


    public void deleteAllUsersQuick() {
        RealmResults<UserQuick> realmResults = mRealm.where(UserQuick.class).findAll();
        if (!realmResults.isEmpty()) {
            mRealm.executeTransaction(realm -> realm.deleteAll());
        }
    }


    public void insertDialogToDB(DialogModel dialog) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(dialog));
    }

    public List<DialogModel> getDialogsByType(int type) {
        return mRealm.where(DialogModel.class).equalTo("type", type).findAllSorted("updatedAt", Sort.DESCENDING);
    }


    public ItemMessage retrieveMessageById(String messageId) {
        return mRealm.where(ItemMessage.class).equalTo(ItemMessage.MESSAGE_ID, messageId).findFirst();
    }

}
