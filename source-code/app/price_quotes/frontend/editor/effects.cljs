
(ns app.price-quotes.frontend.editor.effects
    (:require [app.price-quotes.frontend.editor.views :as editor.views]
              [engines.item-editor.api                :as item-editor]
              [re-frame.api                           :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :price-quotes.editor/download-pdf!
  (fn [{:keys [db]} _]
      (let [price-quote-item (r item-editor/export-current-item db :price-quotes.editor)]
           {:fx [:price-quotes.handler/download-pdf! price-quote-item]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :price-quotes.editor/load-editor!
  {:fx          [:price-quotes.handler/reset-pdf-download!]
   :dispatch-n [[:gestures/init-view-handler! :price-quotes.editor
                                              {:default-view-id :template}]
                [:ui/render-surface! :price-quotes.editor/view
                                     {:content #'editor.views/view}]]})
