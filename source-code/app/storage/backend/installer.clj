
(ns app.storage.backend.installer
    (:require [app.common.backend.api               :as common]
              [app.storage.backend.core.config      :as core.config]
              [io.api                               :as io]
              [mongo-db.api                         :as mongo-db]
              [plugins.git.api                      :as git]
              [re-frame.api                         :as r]
              [x.core.api                           :as x.core]
              [x.media.api                          :as x.media]
              [x.user.api                           :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
(def ROOT-DIRECTORY-DOCUMENT {:media/id          core.config/ROOT-DIRECTORY-ID
                              :media/alias       :my-storage
                              :media/description ""
                              :media/mime-type   "storage/directory"
                              :media/items       [{:media/id core.config/SAMPLE-FILE-ID}]
                              :media/path        []
                              :media/size        0})

; @constant (namespaced map)
(def SAMPLE-FILE-DOCUMENT {:media/id          core.config/SAMPLE-FILE-ID
                           :media/filename    core.config/SAMPLE-FILE-FILENAME
                           :media/alias       "Sample.png"
                           :media/description ""
                           :media/mime-type   "image/png"
                           :media/path        [{:media/id core.config/ROOT-DIRECTORY-ID}]
                           :media/size        0})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  []
  (git/ignore! "!environment/media/thumbnails/default.png" "app.storage")
  (git/ignore! "!environment/media/thumbnails/sample.png"  "app.storage")
  (git/ignore! "!environment/media/storage/sample.png"     "app.storage")
  (let [request {:session x.user/SYSTEM-USER-ACCOUNT}
        options {:prepare-f #(common/added-document-prototype request %)}
        ; Get sample file filesize
        sample-file-filepath (x.media/filename->media-storage-filepath core.config/SAMPLE-FILE-FILENAME)
        sample-file-filesize (io/get-filesize sample-file-filepath)]
       (if-not (mongo-db/get-document-by-id "storage" core.config/SAMPLE-FILE-ID)
               (let [sample-file-document (assoc SAMPLE-FILE-DOCUMENT :media/size sample-file-filesize)]
                    (x.media/generate-thumbnail! core.config/SAMPLE-FILE-FILENAME)
                    (mongo-db/insert-document! "storage" sample-file-document options)))
       (if-not (mongo-db/get-document-by-id "storage" core.config/ROOT-DIRECTORY-ID)
               (let [root-directory-document (assoc ROOT-DIRECTORY-DOCUMENT :media/size sample-file-filesize)]
                    (mongo-db/insert-document! "storage" root-directory-document options)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
