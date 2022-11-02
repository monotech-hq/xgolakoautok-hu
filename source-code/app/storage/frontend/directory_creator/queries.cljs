
(ns app.storage.frontend.directory-creator.queries
    (:require [mid-fruits.candy     :refer [return]]
              [re-frame.api         :refer [r]]
              [x.app-dictionary.api :as x.dictionary]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn check-directory-name
  ; @param (keyword) creator-id
  ; @param (string) directory-name
  ;
  ; @example
  ;  (r check-directory-name db :my-creator "My directory")
  ;  =>
  ;  "My directory"
  ;
  ; @example
  ;  (r check-directory-name db :my-creator "")
  ;  =>
  ;  "New directory"
  ;
  ; @return (string)
  [db [_ _ directory-name]]
  (if-not (empty? directory-name)
          (return directory-name)
          (r x.dictionary/look-up db :new-directory)))

(defn get-create-directory-query
  ; @param (keyword) creator-id
  ; @param (string) directory-name
  ;
  ; @return (vector)
  [db [_ creator-id directory-name]]
  (let [destination-id (get-in db [:storage :directory-creator/meta-items :destination-id])
        alias          (r check-directory-name db creator-id directory-name)]
       [`(storage.directory-creator/create-directory! ~{:destination-id destination-id :alias alias})]))
