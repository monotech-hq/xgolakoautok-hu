
(ns app.storage.backend.installer.config
    (:require [app.storage.backend.core.config :as core.config]))

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
