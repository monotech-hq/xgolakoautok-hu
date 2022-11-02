
(ns app.storage.frontend.media-selector.subs
    (:require [engines.item-browser.api :as item-browser]
              [io.api                   :as io]
              [mid-fruits.candy         :refer [return]]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-selectable?
  [db [_ {:keys [mime-type]}]]
  (if-let [extensions (r item-browser/get-meta-item db :storage.media-selector :extensions)]
          (let [extension (io/mime-type->extension mime-type)]
               (vector/contains-item? extensions extension))
          (return true)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :storage.media-selector/file-selectable? file-selectable?)
