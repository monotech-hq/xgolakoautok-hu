
(ns app.models.frontend.editor.effects
    (:require [app.models.frontend.editor.views :as editor.views]
              [re-frame.api                     :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :models.editor/load-editor!
  {:dispatch-n [[:gestures/init-view-handler! :models.editor
                                              {:default-view-id :data}]
                [:ui/render-surface! :models.editor/view
                                     {:content #'editor.views/view}]]})
