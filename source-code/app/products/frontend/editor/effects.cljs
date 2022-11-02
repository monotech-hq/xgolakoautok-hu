
(ns app.products.frontend.editor.effects
    (:require [app.products.frontend.editor.views :as editor.views]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :products.editor/load-editor!
  {:dispatch-n [[:gestures/init-view-handler! :products.editor
                                              {:default-view-id :data}]
                [:ui/render-surface! :products.editor/view
                                     {:content #'editor.views/view}]]})
