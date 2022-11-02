
(ns templates.price-quotes.blank.info-page)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) template-props
  ;  {}
  [{{:keys [content]} :informations}]
  [:div {:id :blk-info-page}
        [:div {:id :blk-info-page--content}
              [:h5 content]]])
