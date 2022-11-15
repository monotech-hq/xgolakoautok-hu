
(ns app.storage.backend.directory-creator.mutations
    (:require [app.storage.backend.core.side-effects :as core.side-effects]
              [candy.api                             :refer [return]]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-directory-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:alias (string)
  ;   :destination-id (string)}
  ;
  ; @return (namespaced map)
  [env {:keys [alias destination-id]}]
  (if-let [destination-item (mongo-db/get-document-by-id "storage" destination-id)]
          (let [destination-path (get  destination-item :media/path [])
                directory-path   (conj destination-path {:media/id destination-id})
                directory-item {:media/alias alias :media/size 0 :media/description ""
                                :media/items []    :media/path directory-path
                                :media/mime-type "storage/directory"}]
               (when-let [directory-item (core.side-effects/insert-item! env directory-item)]
                         (core.side-effects/attach-item!             env destination-id directory-item)
                         (core.side-effects/update-path-directories! env directory-item +)
                         (return directory-item)))))

(defmutation create-directory!
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'storage.directory-creator/create-directory!}
             (create-directory-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [create-directory!])

(pathom/reg-handlers! ::handlers HANDLERS)
