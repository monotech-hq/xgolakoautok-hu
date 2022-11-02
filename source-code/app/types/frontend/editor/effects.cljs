
(ns app.types.frontend.editor.effects
  (:require [app.types.frontend.editor.views :as editor.views]
            [re-frame.api                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :types.editor/load-editor!
  {:dispatch-n [[:gestures/init-view-handler! :types.editor
                                              {:default-view-id :data}]
                [:ui/render-surface! :types.editor/view
                                     {:content #'editor.views/view}]]})
