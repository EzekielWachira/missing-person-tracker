const functions = require("firebase-functions");
const admin = require("firebase-admin");
// const algoliaSearch = require("algoliaSearch");
//
// const ALGOLIA_APP_ID = "H5IUDB3K8P";
// const ALGOLIA_ADMIN_KEY = "52a0e15361e2ad7424643264190ab5a3";
// const ALGOLIA_INDEX_NAME = "missing_persons";

admin.initializeApp(functions.config().firebase);

exports.sendNotificationOnFoundPerson = functions.firestore
    .document("found_persons/{uid}")
    .onWrite((event) => {
      const missingPersonName = event.after.get("firstname");
      const content = event.after.get("lastname");
      const message = {
        notification: {
          title: missingPersonName,
          body: content,
        },
        topic: "Found Person",
      };
      const response = admin.messaging().send(message);
      console.log(response);
    });

// eslint-disable-next-line max-len
// exports.migrateFirestoreDataToAlgolia = functions.https.onRequest((request, response) => {
//   let mspArr = [];
//
//   admin.firestore().collection("missing_persons").get().then((docs) => {
//     docs.forEach((doc) => {
//       var missingPerson = doc.data();
//       missingPerson.objectID = doc.id;
//       mspArr.push(missingPerson);
//     });
//
//     var client = algoliaSearch(ALGOLIA_APP_ID, ALGOLIA_ADMIN_KEY);
//     var index = client.initIndex(ALGOLIA_INDEX_NAME);
//
//     index.saveObjects(mspArr, (err, content) => {
//       response.status(200).send(content);
//     });
//   });
// });

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
