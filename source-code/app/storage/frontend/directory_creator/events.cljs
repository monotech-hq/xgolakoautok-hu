
(ns app.storage.frontend.directory-creator.events)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-creator-props!
  ; @param (keyword) creator-id
  ; @param (map) creator-props
  ;
  ; @return (map)
  [db [_ _ creator-props]]
  (assoc-in db [:storage :directory-creator/meta-items] creator-props))
