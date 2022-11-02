
(ns app.price-quote-templates.frontend.editor.effects
    (:require [app.price-quote-templates.frontend.editor.views :as editor.views]
              [re-frame.api                                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :price-quote-templates.editor/load-editor!
  {:dispatch-n [[:gestures/init-view-handler! :price-quote-templates.editor
                                              {:default-view-id :data}]
                [:ui/render-surface! :price-quote-templates.editor/view
                                     {:content #'editor.views/view}]]})
