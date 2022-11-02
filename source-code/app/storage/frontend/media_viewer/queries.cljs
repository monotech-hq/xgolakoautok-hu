
(ns app.storage.frontend.media-viewer.queries
    (:require [app.storage.frontend.media-viewer.subs :as media-viewer.subs]
              [re-frame.api                           :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-directory-item-query
  [db [_ viewer-id]]
  (let [directory-id (r media-viewer.subs/get-directory-id db viewer-id)]
       [`(:storage.media-browser/get-item ~{:item-id directory-id})]))
