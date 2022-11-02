
(ns app.storage.backend.installer.side-effects
    (:require [app.common.backend.api               :as common]
              [app.storage.backend.core.config      :as core.config]
              [app.storage.backend.installer.config :as installer.config]
              [io.api                               :as io]
              [mongo-db.api                         :as mongo-db]
              [re-frame.api                         :as r]
              [x.server-media.api                   :as x.media]
              [x.server-user.api                    :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  [_]
  (let [request {:session x.user/SYSTEM-USER-ACCOUNT}
        options {:prepare-f #(common/added-document-prototype request %)}
        ; Get sample file filesize
        sample-file-filepath (x.media/filename->media-storage-filepath core.config/SAMPLE-FILE-FILENAME)
        sample-file-filesize (io/get-filesize sample-file-filepath)]
       (if-not (mongo-db/get-document-by-id "storage" core.config/SAMPLE-FILE-ID)
               (let [sample-file-document (assoc installer.config/SAMPLE-FILE-DOCUMENT :media/size sample-file-filesize)]
                    (x.media/generate-thumbnail! core.config/SAMPLE-FILE-FILENAME)
                    (mongo-db/insert-document! "storage" sample-file-document options)))
       (if-not (mongo-db/get-document-by-id "storage" core.config/ROOT-DIRECTORY-ID)
               (let [root-directory-document (assoc installer.config/ROOT-DIRECTORY-DOCUMENT :media/size sample-file-filesize)]
                    (mongo-db/insert-document! "storage" root-directory-document options)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-fx :storage.installer/check-install! check-install!)
