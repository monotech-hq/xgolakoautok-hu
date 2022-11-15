
(ns app.storage.backend.file-uploader.mutations
    (:require [app.storage.backend.capacity-handler.side-effects :as capacity-handler.side-effects]
              [app.storage.backend.file-uploader.helpers         :as file-uploader.helpers]
              [app.storage.backend.file-uploader.prototypes      :as file-uploader.prototypes]
              [app.storage.backend.core.side-effects             :as core.side-effects]
              [candy.api                                         :refer [return]]
              [com.wsscode.pathom3.connect.operation             :as pathom.co :refer [defmutation]]
              [io.api                                            :as io]
              [mongo-db.api                                      :as mongo-db]
              [pathom.api                                        :as pathom]
              [x.media.api                                       :as x.media]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- upload-file-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:destination-id (string)}
  ; @param (map) file-data
  ;  {:tempfile (?)}
  ;
  ; @return (namespaced map)
  [env {:keys [destination-id]} {:keys [tempfile] :as file-data}]
  (let [file-item (file-uploader.prototypes/file-item-prototype file-data)
        filename  (get file-item :media/filename)
        filepath  (x.media/filename->media-storage-filepath filename)]
       (if (core.side-effects/attach-item! env destination-id file-item)
           (when-let [file-item (core.side-effects/insert-item! env file-item)]
                     ; Copy the temporary file to storage, and delete the temporary file
                     (io/copy-file!               tempfile filepath)
                     (io/delete-file!             tempfile)
                     (x.media/generate-thumbnail! filename)
                     (return                      file-item)))))

(defn- upload-files-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:destination-id (string)}
  ;
  ; @return (namespaced maps in vector)
  [{:keys [request] :as env} {:keys [destination-id] :as mutation-props}]
  (let [total-size (file-uploader.helpers/request->total-size request)
        files-data (file-uploader.helpers/request->files-data request)]
       (if (capacity-handler.side-effects/capacity-limit-exceeded? total-size)
           (return :capacity-limit-exceeded)
           (when-let [destination-item (mongo-db/get-document-by-id "storage" destination-id)]
                     (let [destination-path (get  destination-item :media/path [])
                           item-path        (conj destination-path {:media/id destination-id})]
                          ; A feltöltés véglegesítése előtt lefoglalja a szükséges tárhely-kapacitást,
                          ; így az egyszerre történő feltöltések nem léphetik át a megengedett tárhely-kapacitást ...
                          (core.side-effects/update-path-directories! env {:media/size total-size :media/path item-path} +)
                          (letfn [(f [result _ file-data]
                                     (let [file-data (assoc file-data :file-path item-path)]
                                          (conj result (upload-file-f env mutation-props file-data))))]
                                 (reduce-kv f [] files-data)))))))

(defmutation upload-files!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:destination-id (string)}
             ;
             ; @return (namespaced maps in vector)
             [env mutation-props]
             {::pathom.co/op-name 'storage.file-uploader/upload-files!}
             (upload-files-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [upload-files!])

(pathom/reg-handlers! ::handlers HANDLERS)
